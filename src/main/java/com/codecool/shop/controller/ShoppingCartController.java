package com.codecool.shop.controller;

import org.json.JSONObject;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.ShoppingCart;
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

@WebServlet(urlPatterns = "/shoppingcart")
public class ShoppingCartController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ShoppingCart shoppingCart = getShoppingCart(request);
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());
        context.setVariable("shoppingcart", shoppingCart);
        engine.process("shoppingcart/shoppingcart.html", context, response.getWriter());
    }

    private ShoppingCart getShoppingCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.isNew()) session.setAttribute("UserObject", new User());
        User user = (User) session.getAttribute("UserObject");
        return user.shoppingCart;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ShoppingCart shoppingCart = getShoppingCart(request);
        int id = Integer.parseInt(request.getParameter("id"));
        if (request.getParameter("process").equals("increase")) {
            shoppingCart.addItem(id);
        } else {
            shoppingCart.removeItem(id);
        }
        JSONObject json = new JSONObject();
        json.put("numOfItems", shoppingCart.getNumberOfItemById(id));
        json.put("total", shoppingCart.sumCart());

        response.setContentType("application/json");
        response.getWriter().print(json);
    }

}
