package com.codecool.shop.model;

import com.codecool.shop.dao.implementation.ShoppingCartDaoDB;

import java.util.ArrayList;
import java.util.List;

public class User {
    public List<Order> orders = new ArrayList<>();
    private int shoppingCartID;

    public User () {
        ShoppingCartDaoDB shoppingCartDaoDB = ShoppingCartDaoDB.getInstance();
        shoppingCartID = shoppingCartDaoDB.getNewId();
    }

    public int getShoppingCartID() {
        return shoppingCartID;
    }

    public void setShoppingCartID(int shoppingCartID) {
        this.shoppingCartID = shoppingCartID;
    }
}
