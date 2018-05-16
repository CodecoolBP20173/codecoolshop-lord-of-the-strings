package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDaoMem implements ProductDao {

    private List<Product> data = new ArrayList<>();
    private static ProductDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductDaoMem() {
    }

    public static ProductDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        product.setId(data.size() + 1);
        data.add(product);
    }

    @Override
    public Product find(int id) {
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public void removeAllProducts() {
        data.clear();
    }

    @Override
    public List<Product> getAll() {
        return data;
    }


    /**
     * Gets all products by supplier
     * @param supplier supplier object with id, name etc.
     * @return list of products
     */
    @Override
    public List<Product> getBy(Supplier supplier) {
        return data.stream().filter(t -> t.getSupplier().equals(supplier)).collect(Collectors.toList());
    }

    /**
     * Gets all products by category
     * @param productCategory productCategory object with id, name, etc.
     * @return a list of products
     */
    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return data.stream().filter(t -> t.getProductCategory().equals(productCategory)).collect(Collectors.toList());
    }

    /**
     * Get all products by supplier and category
     * @param supplier a mentor with name, id, etc.
     * @param category
     * @return a list of products
     */
    @Override
    public List<Product> getBy(Supplier supplier, ProductCategory category) {
        return data.stream().filter(t -> t.getProductCategory().equals(category) && t.getSupplier().equals(supplier)).collect(Collectors.toList());
    }
}
