package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoDB implements ProductCategoryDao, Queryhandler {

    private static final String connectionConfigPath = "src/main/resources/connection.properties";

    @Override
    public void add(ProductCategory category) {
        String query = "ISNERT INTO product_categories (name, description, department) VALUES" +
                " (?, ?, ?);";
        List<Object> parameters = new ArrayList<>();
        parameters.add(category.getName());
        parameters.add(category.getDescription());
        parameters.add(category.getDepartment());
        executeDMLQuery(query, parameters);
    }

    @Override
    public ProductCategory find(int id) {
        String query = "SELECT * FROM product_categories WHERE id=?;";
        List<Object> parameters = new ArrayList<>();
        parameters.add(id);
        ResultSet resultSet = executeSelctQuery(query, parameters);

        ProductCategory result = null;
        try {
            resultSet.next();
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String department = resultSet.getString("department");
            result = new ProductCategory(name, department, description);
            result.setId(id);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM product_categories WHERE id=?;";
        List<Object> parameters = new ArrayList<>();
        parameters.add(id);
        executeDMLQuery(query, parameters);
    }

    @Override
    public Integer findIdByName(String name) {
        String query = "SELECT * FROM product_categories WHERE name=?;";
        List<Object> parameters = new ArrayList<>();
        parameters.add(name);
        ResultSet resultSet = executeSelctQuery(query, parameters);

        List<Integer> results = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                results.add(Integer.parseInt(id));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        Integer result = null;
        try {
            result = results.get(0);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ProductCategory getDefaultCategory() {
        return new ProductCategory("All", "", "");
    }

    @Override
    public List<Product> filterProducts(List<Product> products, ProductCategory category) {
        if (category.equals(getDefaultCategory())) {
            return products;
        }
        List<Product> temp = new ArrayList<>();
        for (Product product : products) {
            if (product.getProductCategory().equals(category)) {
                temp.add(product);
            }
        }
        return temp;
    }

    @Override
    public List<ProductCategory> getAll() {
        String query = "SELECT * FROM product_categories;";
        ResultSet resultSet = executeSelectQuery(query);

        List<ProductCategory> results = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String department = resultSet.getString("department");
                ProductCategory temp = new ProductCategory(name, department, description);
                temp.setId(Integer.parseInt(id));
                results.add(temp);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return results;
    }

    @Override
    public String getConnectionConfigPath() {
        return connectionConfigPath;
    }

    @Override
    public void setConnectionConfigPath() {

    }
}
