package com.codecool.shop.controller;

import com.codecool.shop.dao.implementation.*;
import org.json.JSONObject;

import com.codecool.shop.model.User;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.*;

import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/snake-products"})
public class SnakeProductController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        ProductDao productDAO = ProductDaoDB.getInstance();
        ProductCategoryDao productCategoryDAO = ProductCategoryDaoDB.getInstance();

        ProductCategory special = productCategoryDAO.find(productCategoryDAO.findIdByName("Special skills"));
        List<Product> products = productDAO.getBy(special);

        JSONObject json = new JSONObject();

        Map<Integer, Integer> productData = new HashMap<>();
        List<Integer> productIdList = new ArrayList<>();

        for (Product currentProcduct : products){
            productData.put(currentProcduct.getId(), currentProcduct.getDefaultPrice());
            productIdList.add(currentProcduct.getId());
        }

        json.put("productIdList", productIdList);
        json.put("productData", productData );

        response.setContentType("application/json");
        response.getWriter().print(json);
    }
}
