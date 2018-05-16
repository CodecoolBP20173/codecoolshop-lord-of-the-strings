package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductCategoryDaoDB implements ProductCategoryDao, Queryhandler {

    private String connectionConfigPath = "src/main/resources/connection.properties";
    private static ProductCategoryDaoDB instance = null;

    public static ProductCategoryDaoDB getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoDB();
        }
        return instance;
    }

    public ProductCategoryDaoDB(String connectionConfigPath) {
        this.connectionConfigPath = connectionConfigPath;
    }

    public ProductCategoryDaoDB() {
    }

    @Override
    public void add(ProductCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("Null category can not be added.");
        } else if ("".equals(category.getName())){
            throw new ValueException("Category must have a name.");
        } else if ("".equals(category.getDepartment())){
            throw new ValueException("Category must have a department.");
        } else if ("".equals(category.getDescription())){
            throw new ValueException("Category must have a description.");
        }

        String query = "INSERT INTO product_categories (name, description, department) VALUES" +
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
        List<Map<String, Object>> resultList = executeSelectQuery(query, parameters);

        ProductCategory result = null;

        if (resultList.size() == 1) {
            for (Map<String, Object> resultSet : resultList) {
                String name = resultSet.get("name").toString();
                String description = resultSet.get("description").toString();
                String department = resultSet.get("department").toString();
                result = new ProductCategory(name, department, description);
                result.setId(id);
            }
        }

        return result;
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM product_categories WHERE id=?;";
        List<Object> parameters = new ArrayList<>();
        parameters.add(id);

        String tempQuery = "SELECT * FROM product_categories WHERE id=?;";
        List<Map<String, Object>> resultList = executeSelectQuery(tempQuery, parameters);
        if (resultList.size() == 0){
            throw new IllegalArgumentException("There is no product category with such id in the database.");
        }

        Integer result = executeDMLQuery(query, parameters);

    }

    @Override
    public void removeAll() {
        String query = "DELETE from product_categories;";
        executeDMLQuery(query);
    }

    @Override
    public Integer findIdByName(String name) {
        String query = "SELECT * FROM product_categories WHERE name=?;";
        List<Object> parameters = new ArrayList<>();
        parameters.add(name);
        List<Map<String, Object>> resultList = executeSelectQuery(query, parameters);

        Integer result = null;
        try {
            result = Integer.parseInt(resultList.get(0).get("id").toString());
        } catch (IndexOutOfBoundsException ex){
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
    public List<ProductCategory> getAll() {
        String query = "SELECT * FROM product_categories;";
        List<Map<String, Object>> resultList = executeSelectQuery(query);

        List<ProductCategory> results = new ArrayList<>();

        for (Map<String, Object> resultSet : resultList) {
            String id = resultSet.get("id").toString();
            String name = resultSet.get("name").toString();
            String description = resultSet.get("description").toString();
            String department = resultSet.get("department").toString();
            ProductCategory temp = new ProductCategory(name, department, description);
            temp.setId(Integer.parseInt(id));
            results.add(temp);
        }

        return results;
    }

    @Override
    public String getConnectionConfigPath() {
        return connectionConfigPath;
    }
}
