package com.codecool.shop.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnakeCartInitControllerTest {

    @Test
    void test_doPost_happy_path() {
        HttpRequester requester = new HttpRequester("http://localhost:8080/snake-cart");
        String cartID = requester.rawPostRequest("");
        assertEquals("5", cartID);
    }

}