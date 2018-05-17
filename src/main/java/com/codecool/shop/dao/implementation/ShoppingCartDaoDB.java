package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.Timestamp;

public class ShoppingCartDaoDB implements ShoppingCartDao, Queryhandler {
    private HashMap<Product, Integer> shoppingCartContent = new HashMap<Product, Integer>();
    private String connection_config_path = "src/main/resources/connection.properties";
    private static ShoppingCartDaoDB instance = null;

    public static ShoppingCartDaoDB getInstance() {
        if (instance == null) {
            instance = new ShoppingCartDaoDB();
        }
        return instance;
    }

    public ShoppingCartDaoDB(String connection_config_path) {
        this.connection_config_path = connection_config_path;
    }

    public ShoppingCartDaoDB() {
    }

    public void addItem(int productId, int shoppingCartId) {
        String query = "INSERT INTO shopping_cart_product (shopping_cart_id, product_id) VALUES (?, ?)";
        List<Object> parameters = new ArrayList<>();
        parameters.add(shoppingCartId);
        parameters.add(productId);
        executeDMLQuery(query, parameters);
    }

    public void removeItem(int productId, int shoppingCartId) {
        Integer deleteId = this.getFirstIdOfProductFromSameShoppingCart(productId, shoppingCartId);
        String query = "DELETE FROM shopping_cart_product WHERE id = ?";
        List<Object> parameters = new ArrayList<>();
        parameters.add(deleteId);
        executeDMLQuery(query, parameters);
    }

    public int getFirstIdOfProductFromSameShoppingCart(int productId, int shoppingCartId) {
        String query = "SELECT * FROM shopping_cart_product WHERE shopping_cart_id = ? AND product_id = ?";
        List<Object> parameters = new ArrayList<>();
        parameters.add(shoppingCartId);
        parameters.add(productId);
        List<Map<String, Object>> result = executeSelectQuery(query, parameters);
        if (result.size() == 0) {
            return 0;
        } else {
            return (int) result.get(0).get("id");
        }

    }

    public int sumCart(int shopping_cart_id) {
        String query = "SELECT SUM(default_price) FROM products " +
                "INNER JOIN shopping_cart_product ON products.id = shopping_cart_product.product_id " +
                "WHERE shopping_cart_id = ?;";
        List<Object> parameters = new ArrayList<>();
        parameters.add(shopping_cart_id);
        List<Map<String, Object>> result = executeSelectQuery(query, parameters);
        try {
            return ((Long) result.get(0).get("sum")).intValue();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public void clear() {
        shoppingCartContent.clear();
    }

    public String getConnectionConfigPath() {
        return connection_config_path;
    }

    public int getNewId() {
        this.newShoppingCart();
        List<Map<String, Object>> shoppingCart = this.getNewShoppingCart();
        return (Integer) shoppingCart.get(0).get("id");
    }

    private List<Map<String, Object>> getNewShoppingCart() {
        String query = "SELECT * FROM shopping_carts WHERE status = ?";
        List<Object> parameters = new ArrayList<>();
        parameters.add(Statuses.NEW.toString());
        List<Map<String, Object>> result = executeSelectQuery(query, parameters);
        this.changeShoppingCartStatusToOld();
        return result;

    }

    private void changeShoppingCartStatusToOld() {
        String query = "UPDATE shopping_carts SET status = ? WHERE status = ?";
        List<Object> parameters = new ArrayList<>();
        parameters.add(Statuses.OLD.toString());
        parameters.add(Statuses.NEW.toString());
        executeDMLQuery(query, parameters);
    }

    private void newShoppingCart() {
        String query = "INSERT INTO shopping_carts (creation_date, status) VALUES (?, ?)";
        List<Object> parameters = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis());
        parameters.add(timestamp);
        parameters.add(Statuses.NEW.toString());
        executeDMLQuery(query, parameters);
    }

    public Integer getQuantityOfProductById(int productId, int shoppingCartId) {
        String query = "SELECT COUNT(product_id) FROM shopping_cart_product WHERE shopping_cart_id = ? AND product_id = ? " +
                "GROUP BY product_id";
        List<Object> parameters = new ArrayList<>();
        parameters.add(shoppingCartId);
        parameters.add(productId);
        List<Map<String, Object>> result = executeSelectQuery(query, parameters);
        if (result.size() == 0) {
            return 0;
        } else {
            return ((Long) result.get(0).get("count")).intValue();
        }
    }

    @Override
    public int getNumberOfItems(int shoppingCartId) {
        String query = "SELECT * FROM shopping_cart_product WHERE shopping_cart_id = ?";
        List<Object> parameters = new ArrayList<>();
        parameters.add(shoppingCartId);
        List<Map<String, Object>> result = executeSelectQuery(query, parameters);
        return result.size();
    }

    public HashMap<Product, Integer> getContent(int shoppingCartId) {
        HashMap<Product, Integer> allProduct = new HashMap<>();
        String query = "SELECT DISTINCT name, description, " +
                "default_price, default_currency, product_category, " +
                "supplier, shopping_cart_id, product_id FROM products " +
                "INNER JOIN shopping_cart_product ON products.id = shopping_cart_product.product_id " +
                "WHERE shopping_cart_id = ?;";
        List<Object> parameters = new ArrayList<>();
        parameters.add(shoppingCartId);
        List<Map<String, Object>> result = executeSelectQuery(query, parameters);
        SupplierDaoDB supplierDaoDB = new SupplierDaoDB();
        ProductCategoryDaoDB productCategoryDaoDB = new ProductCategoryDaoDB();
        for (Map<String, Object> product : result) {
            Integer quantityOfProduct = this.getQuantityOfProductById((Integer) product.get("product_id"), shoppingCartId);
            Product newProduct = new Product(
                    (String) product.get("name"),
                    (Integer) product.get("default_price"),
                    (String) product.get("default_currency"),
                    (String) product.get("description"),
                    productCategoryDaoDB.find((Integer) product.get("product_category")),
                    supplierDaoDB.find((Integer) product.get("supplier"))
            );
            newProduct.setId((Integer) product.get("product_id"));
            allProduct.put(newProduct, quantityOfProduct);
        }
        return allProduct;
    }


    private enum Statuses {
        NEW, OLD
    }
}
