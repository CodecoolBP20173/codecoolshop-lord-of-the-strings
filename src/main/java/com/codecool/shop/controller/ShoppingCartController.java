package com.codecool.shop.controller;

import com.codecool.shop.dao.ShoppingCartDao;
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

@WebServlet (urlPatterns = "/shoppingcart")
public class ShoppingCartController extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.isNew()) {
            session.setAttribute("UserObject", new User());
        }
        User user = (User)session.getAttribute("UserObject");

        int shoppingCartID = user.getShoppingCartID();



        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());

        ShoppingCartDaoDB shoppingCartDaoDB = new ShoppingCartDaoDB();
        HashMap<Product, Integer> shoppingCartContent = shoppingCartDaoDB.getContent(shoppingCartID);
        context.setVariable("shoppingcart", shoppingCartContent);
        context.setVariable("shoppingCartDaoDB", shoppingCartDaoDB);
        context.setVariable("shoppingCartId", shoppingCartID);

        engine.process("shoppingcart/shoppingcart.html", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.isNew()) {
            session.setAttribute("UserObject", new User());
        }
        User user = (User)session.getAttribute("UserObject");

        int shoppingCartId = user.getShoppingCartID();

        int productId = Integer.parseInt(request.getParameter("id"));
        ShoppingCartDaoDB shoppingCartDaoDB = new ShoppingCartDaoDB();

        if (request.getParameter("process").equals("increase")) {
            shoppingCartDaoDB.addItem(productId, shoppingCartId);
        } else {
            shoppingCartDaoDB.removeItem(productId, shoppingCartId);
        }

        JSONObject json = new JSONObject();
        json.put("numOfItems", shoppingCartDaoDB.getQuantityOfProductById(productId, shoppingCartId));
        json.put("total", shoppingCartDaoDB.sumCart(shoppingCartId));

        response.setContentType("application/json");
        response.getWriter().print(json);
    }

}
