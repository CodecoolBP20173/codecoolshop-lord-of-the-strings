package com.codecool.shop.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SnakeProductControllerTest {

    @Test
    void test_doPost_happy_path() {
        HttpRequester requester = new HttpRequester("http://localhost:8080/snake-products");
        String serverResponse = requester.sendPostRequest("");
        Map<Integer, Integer> productData = requester.parseJsonToMapProducts(serverResponse);
        int[] resultList = {10, 10, 10, 10, 10};
        int idOffset = 31;
        for (int i = 0; i < 5 ; i++) {
            Integer key = i + idOffset;
            Integer result = resultList[i];
            assertEquals(result, productData.get(key));
        }
    }
}