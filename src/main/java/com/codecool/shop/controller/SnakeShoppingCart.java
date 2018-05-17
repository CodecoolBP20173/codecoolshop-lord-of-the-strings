package com.codecool.shop.controller;

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

@WebServlet (urlPatterns = "/snake-shopping-cart")
public class SnakeShoppingCart extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.isNew()) {
            session.setAttribute("UserObject", new User());
        }
        User user = (User)session.getAttribute("UserObject");

        int shoppingCartId = user.getShoppingCartID();



        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());

        context.setVariable("shoppingcart", shoppingCartId);

        engine.process("snake_shoppingcart/snake_shoppingcart.html", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.isNew()) {
            session.setAttribute("UserObject", new User());
        }
        JSONObject json = new JSONObject();
//        json.put("numOfItems", shoppingCart.getNumberOfItemById(id));
//        json.put("total", shoppingCart.sumCart());

        response.setContentType("application/json");
        response.getWriter().print(json);
    }

}
