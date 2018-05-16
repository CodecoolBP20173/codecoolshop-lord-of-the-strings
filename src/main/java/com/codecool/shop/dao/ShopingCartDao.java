package com.codecool.shop.dao;

public interface ShoppingCartDao {
    void addItem(int id);
    void removeItem(int id);
    int sumCart();
    int getNumberOfItemById(int id);
    int getNumberOfItems();

}
