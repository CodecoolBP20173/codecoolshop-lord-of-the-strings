package com.codecool.shop.model;

import com.codecool.shop.dao.implementation.ShoppingCartDaoDB;

import java.util.ArrayList;
import java.util.List;

public class User {
    public List<Order> orders = new ArrayList<>();
    private int shoppingCartID;
    ShoppingCartDaoDB shoppingCartDaoDB = ShoppingCartDaoDB.getInstance();

    public User() {
        shoppingCartID = shoppingCartDaoDB.getNewId();
    }

    public int getShoppingCartID() {
        return shoppingCartID;
    }

    public void getNewShoppingCart() {
        shoppingCartID = shoppingCartDaoDB.getNewId();
    }
}
