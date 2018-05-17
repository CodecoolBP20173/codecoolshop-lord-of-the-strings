package com.codecool.shop.dao;

import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;

import java.util.List;

public interface ProductCategoryDao {

    void add(ProductCategory category);
    ProductCategory find(int id);
    void remove(int id);
    void removeAll();
    Integer findIdByName(String name);
    ProductCategory getDefaultCategory();
    List<ProductCategory> getAll();

}
