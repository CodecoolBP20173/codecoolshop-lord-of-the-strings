package com.codecool.shop.dao;

import com.codecool.shop.dao.implementation.ProductCategoryDaoDB;
import com.codecool.shop.dao.implementation.ProductDaoDB;
import com.codecool.shop.dao.implementation.SupplierDaoDB;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductDaoTest {

    private ProductDaoDB productDaoDB;
    private Product productToAdd;
    private int numberOfProducts;
    
    @BeforeEach
    void setup() {
        productDaoDB = new ProductDaoDB("test_resources/connection.properties");
        this.numberOfProducts = productDaoDB.numberOfProducts();
        ProductCategoryDaoDB productCategoryDaoDB = new ProductCategoryDaoDB("test_resources/connection.properties");
        int categoryId = productCategoryDaoDB.findIdByName("animal");
        ProductCategory animal = productCategoryDaoDB.find(categoryId);
        SupplierDaoDB supplierDaoDB = new SupplierDaoDB("test_resources/connection.properties");
        Supplier zoo = supplierDaoDB.find(supplierDaoDB.findIdByName("Állatkert"));
        productToAdd = new Product("rénszarvas", 77, "HUF", "Egy döglött rénszarvas", animal, zoo);
    }

    @Test
    void testProductNumberGrowsOnAdd() {
        int numberOfProducts = productDaoDB.numberOfProducts();
        productDaoDB.add(productToAdd);
        int numberOfProductsAfter = productDaoDB.numberOfProducts();
        assertTrue(numberOfProductsAfter - numberOfProducts == 1);
    }

    @Test
    void testFindProduct() {
        Product productInDB = productDaoDB.find(3);
        assertEquals("fikusz", productInDB.getName());
    }

    @Test
    void testRemoveNumberDecreaseOnRemove() {
        productDaoDB.remove(5);
        int numberOfProductsAfter = productDaoDB.numberOfProducts();
        assertTrue(numberOfProducts - numberOfProductsAfter == 1);
    }

    @Test
    void testGetAll() {
        assertEquals(4, numberOfProducts);
    }

    @Test
    void testGetBySupplier() {
        SupplierDaoDB supplierDaoDB = new SupplierDaoDB("test_resources/connection.properties");
        Supplier zoo = supplierDaoDB.find(supplierDaoDB.findIdByName("Füvészkert"));
        assertEquals(2, productDaoDB.getBy(zoo).size());
    }

    @Test
    void testGetByProductCategory() {
        ProductCategoryDaoDB productCategoryDaoDB = new ProductCategoryDaoDB("test_resources/connection.properties");
        ProductCategory animal = productCategoryDaoDB.find(productCategoryDaoDB.findIdByName("plant"));
        assertEquals(2, productDaoDB.getBy(animal).size());
    }
}