package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.model.Product;

import java.awt.image.AreaAveragingScaleFilter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.util.stream.Stream;

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
        String query = "INSERT INTO shopping_carts_product (shopping_cart_id, product_id) VALUES (?, ?)";
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
        return (int) result.get(0).get("id");

    }

    public HashMap getContent() {
        return shoppingCartContent;
    }

    public int sumCart(int shopping_cart_id) {
        String query = "SELECT SUM(default_price) FROM products" +
                "INNER JOIN shopping_cart_product ON products.id = shopping_cart_product.product_id" +
                "WHERE shopping_cart_id = ?;";
        List<Object> parameters = new ArrayList<>();
        parameters.add(shopping_cart_id);
        List<Map<String, Object>> result = executeSelectQuery(query, parameters);
        return (int) result.get(0).get("sum");
    }

    public int getNumberOfItemById(int id) {
        Product productToCount = ProductDaoDB.getInstance().find(id);
        Product sameProduct = getSameProductFromShoppingCartContent(productToCount);
        if (sameProduct == null) return 0;
        return shoppingCartContent.get(sameProduct);
    }

    private Product getSameProductFromShoppingCartContent(Product searchedProduct) {
        for (Product product : shoppingCartContent.keySet()) {
            if (product.getId() == searchedProduct.getId()) return product;
        }
        return null;
    }

    public int getNumberOfItems() {
        return shoppingCartContent.values().stream().mapToInt(i -> i).sum();
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
        String query = "SELECT * FROM shopping_carts WHERE statuses = ?";
        List<Object> parameters = new ArrayList<>();
        parameters.add(Statuses.NEW.toString());
        List<Map<String, Object>> result = executeSelectQuery(query, parameters);
        this.changeShoppinCartStatusToOld();
        return result;

    }

    private void changeShoppinCartStatusToOld() {
        String query = "UPDATE shopping_carts SET statuses = ? WHERE statuses = ?";
        List<Object> parameters = new ArrayList<>();
        parameters.add(Statuses.OLD.toString());
        parameters.add(Statuses.NEW.toString());
        executeDMLQuery(query, parameters);
    }

    private void newShoppingCart() {
        String query = "INSERT INTO shopping_carts (creation_date, status) VALUES (?, ?)";
        List<Object> parameters = new ArrayList<>();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        parameters.add(timestamp.getTime());
        parameters.add(Statuses.NEW.toString());
        executeDMLQuery(query, parameters);
    }

    private Integer getQuantityOfProduct(int productId) {
        String query = "SELECT * FROM shopping_cart_product WHERE product_id = ?";
        List<Object> parameters = new ArrayList<>();
        parameters.add(productId);
        List<Map<String, Object>> result = executeSelectQuery(query, parameters);
        return result.size();
    }

    private enum Statuses {
        NEW, OLD
    }
}
