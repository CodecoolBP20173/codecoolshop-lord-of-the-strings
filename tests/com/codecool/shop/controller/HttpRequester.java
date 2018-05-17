package com.codecool.shop.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

public class HttpRequester {
    private HttpURLConnection connection = null;

    public HttpRequester(String url) {
        try {
            URL UrlObj = new URL(url);
            connection = (HttpURLConnection) UrlObj.openConnection();
        } catch (MalformedURLException e) {
            System.out.println("Wrong URL string");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Cannot connect URL");
            e.printStackTrace();
        }
    }

    public String rawPostRequest(String parameters) {
        try {
            composePostRequestHeader(parameters);

            DataOutputStream connectionDataOutputStream = new DataOutputStream(connection.getOutputStream());
            connectionDataOutputStream.writeBytes(parameters);
            connectionDataOutputStream.flush();

            int responseCode = connection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + connection.getURL());
            System.out.println("Post content type : " + connection.getRequestProperty("Content-Type"));
            System.out.println("Post parameters : " + parameters);
            System.out.println("Response Code : " + responseCode);
        } catch (IOException e) {
            System.out.println("Error sending data");
        }

        StringBuilder serverResponse = new StringBuilder();

        try (BufferedReader connectionDataInputStream = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));) {
            String inputLine;
            while ((inputLine = connectionDataInputStream.readLine()) != null) {
                serverResponse.append(inputLine);
            }
        } catch (IOException e) {
            System.out.println("Error receiving data");
        }
        return serverResponse.toString();
    }

    public Map<Integer, Integer> sendPostRequest(String parameters) {
        try {
            composePostRequestHeader(parameters);

            DataOutputStream connectionDataOutputStream = new DataOutputStream(connection.getOutputStream());
            connectionDataOutputStream.writeBytes(parameters);
            connectionDataOutputStream.flush();

            int responseCode = connection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + connection.getURL());
            System.out.println("Post content type : " + connection.getRequestProperty("Content-Type"));
            System.out.println("Post parameters : " + parameters);
            System.out.println("Response Code : " + responseCode);
        } catch (IOException e) {
            System.out.println("Error sending data");
        }

        StringBuilder serverResponse = new StringBuilder();

        try (BufferedReader connectionDataInputStream = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));) {
            String inputLine;
            while ((inputLine = connectionDataInputStream.readLine()) != null) {
                serverResponse.append(inputLine);
            }
        } catch (IOException e) {
            System.out.println("Error receiving data");
        }

        String jsonString = serverResponse.toString();

        return parseJsonToMapProducts(jsonString);
    }

    private Map<String, Integer> parseJsonToMap(String serverResponse) {
        JsonElement element = new JsonParser().parse(serverResponse);
        JsonObject object = element.getAsJsonObject();
        int numberOfItems = object.get("numberOfItems").getAsInt();
        int totalPrice = object.get("priceSum").getAsInt();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("itemsNum", numberOfItems);
        map.put("totalPrice", totalPrice);
        return map;
    }

    private Map<Integer, Integer> parseJsonToMapProducts(String serverResponse) {
        JsonElement element = new JsonParser().parse(serverResponse);
        JsonObject object = element.getAsJsonObject();

        JsonArray productIdList = object.get("productIdList").getAsJsonArray();
        JsonObject productDataObject = object.get("productData").getAsJsonObject();

        Map<Integer, Integer> productData = new HashMap<>();

        for (int i = 0; i < productIdList.size(); i++) {
            Integer index = productIdList.get(i).getAsInt();
            productData.put(index, productDataObject.get(index.toString()).getAsInt());
        }
        return productData;
    }

    private void composePostRequestHeader(String urlParameters) {
        try {
            connection.setRequestMethod("POST");
            //connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Language", "en-US");
            //connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setDoOutput(true);
            connection.setDoInput(true);
        } catch (ProtocolException e) {
            System.out.println("Protocol exception");
            e.printStackTrace();
        }
    }
}

