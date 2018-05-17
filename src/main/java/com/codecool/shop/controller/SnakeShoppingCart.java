package com.codecool.shop.controller;

import com.codecool.shop.model.Product;
import org.json.JSONObject;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.ShoppingCartDaoDB;
import com.codecool.shop.model.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@WebServlet (urlPatterns = "/snake-shopping-cart")
public class SnakeShoppingCart extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int shoppingCartID = Integer.parseInt(request.getParameter("shoppingcart_id"));

        ShoppingCartDaoDB shoppingCartDaoDB = new ShoppingCartDaoDB();
        HashMap<Product, Integer> shoppingCartContent = shoppingCartDaoDB.getContent(shoppingCartID);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());

        context.setVariable("shoppingcart", shoppingCartContent);
        context.setVariable("shoppingCartDaoDB", shoppingCartDaoDB);
        context.setVariable("shoppingCartId", shoppingCartID);

        engine.process("snake_shoppingcart/snake_shoppingcart.html", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

}
