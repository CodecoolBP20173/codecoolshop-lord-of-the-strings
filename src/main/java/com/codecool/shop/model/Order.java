package com.codecool.shop.model;

import com.codecool.shop.dao.implementation.ShoppingCartDaoDB;

public class Order {

    public static enum Status {
        IN_PROGRESS,
        PAID
    }

    private String name;
    private String email;
    private Address billingAddress;
    private Address shippingAddress;
    private int phone;
    private Status status;
    int orderShoppingCartId;

    public Order(String name, String email, Address billingAddress, Address shippingAddress, int phone, int shoppingCartId) {
        this.orderShoppingCartId = shoppingCartId;
        this.name = name;
        this.email = email;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        this.phone = phone;
        status = Status.IN_PROGRESS;
    }

    public void pay() {
        status = Status.PAID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public Status getStatus() {
        return status;
    }

}
