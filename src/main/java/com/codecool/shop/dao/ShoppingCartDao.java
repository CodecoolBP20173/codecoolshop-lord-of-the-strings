package com.codecool.shop.dao;

public interface ShoppingCartDao {
    void addItem(int productId, int shoppingCartId);
    void removeItem(int productId, int shoppingCartId);
    int sumCart(int shopping_cart_id);
    Integer getQuantityOfProductById(int productId, int shoppingCartId);
    int getNumberOfItems(int shoppingCartId);
}
