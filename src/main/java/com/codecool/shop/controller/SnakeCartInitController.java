package com.codecool.shop.controller;

import com.codecool.shop.dao.implementation.*;
import org.json.JSONObject;

import com.codecool.shop.model.User;
import com.codecool.shop.model.ShoppingCart;

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
import java.util.List;

@WebServlet(urlPatterns = {"/snake-cart"})
public class SnakeCartInitController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

//        ShoppingCart snakeCart = new ShoppingCartDaoDB();
//
//        JSONObject json = new JSONObject();
//        json.put("shoppingCartId", snakeCart.getNewId() );

        Integer shoppingCartId = 5;

        response.setContentType("text");
        response.getWriter().print(shoppingCartId.toString());
    }
}
