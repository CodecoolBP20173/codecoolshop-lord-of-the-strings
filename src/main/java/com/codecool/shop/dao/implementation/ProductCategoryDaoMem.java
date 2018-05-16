package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoMem implements ProductCategoryDao {

    private List<ProductCategory> data = new ArrayList<>();
    private static ProductCategoryDaoMem instance = null;
    private ProductCategory defaultCategory;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductCategoryDaoMem() {
        defaultCategory = new ProductCategory("All", "", "");
    }

    public static ProductCategoryDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoMem();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {
        category.setId(data.size() + 1);
        data.add(category);
    }

    @Override
    public ProductCategory find(int id) {
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public void removeAll() {
        data.clear();
    }

    @Override
    public List<ProductCategory> getAll() {
        return data;
    }

    @Override
    public Integer findIdByName(String name) {
        for (ProductCategory category : data) {
            if (name.equals(category.getName())) {
                return category.getId();
            }
        }
        return null;
    }

    @Override
    public ProductCategory getDefaultCategory() {
        return defaultCategory;
    }

    public List<Product> filterProducts(List<Product> products, ProductCategory category) {
        if (category.equals(defaultCategory)) {
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
}
