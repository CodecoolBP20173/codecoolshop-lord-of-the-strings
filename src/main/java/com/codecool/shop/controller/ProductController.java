package com.codecool.shop.controller;

import com.codecool.shop.dao.implementation.*;
import org.json.JSONObject;

import com.codecool.shop.model.User;
import com.codecool.shop.dao.implementation.ShoppingCartDaoDB;

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
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());


        SupplierDao supplierDataStore = SupplierDaoDB.getInstance();
        ProductDao productDataStore = ProductDaoDB.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoDB.getInstance();


        ProductCategory category;
        Supplier supplier;

        String selectedCategory = req.getParameter("select_category");
        if (selectedCategory != null &&
                !selectedCategory.equals(productCategoryDataStore.getDefaultCategory().getName())) {
            category = productCategoryDataStore.find(
                    productCategoryDataStore.findIdByName(
                            req.getParameter("select_category")));
        } else {
            category = productCategoryDataStore.getDefaultCategory();
        }

        String selectedSupplier = req.getParameter("select_supplier");
        if (selectedSupplier != null &&
                !selectedSupplier.equals(supplierDataStore.getDefaultSupplier().getName())) {
            supplier = supplierDataStore.find(
                    supplierDataStore.findIdByName(
                            req.getParameter("select_supplier")));
        } else {
            supplier = supplierDataStore.getDefaultSupplier();
        }

        List<Product> products = supplierDataStore.filterProducts(
                productCategoryDataStore.filterProducts(
                        productDataStore.getAll(), category), supplier);


        if (req.getParameter("ajax") != null) {
            JSONObject json = new JSONObject();
            int numberOfProducts = 0;
            for (Product product : products) {

                json.put("Product" + numberOfProducts, new JSONObject()
                        .put("title", product.getName())
                        .put("description", product.getDescription())
                        .put("id", product.getId())
                        .put("price", product.getPrice())
                        .put("supplier", product.getSupplier().getName()));
                numberOfProducts++;
            }

            resp.setContentType("application/json");
            resp.getWriter().print(json);

        } else {
            ShoppingCartDaoDB shoppingCart = getShoppingCart(req);

            context.setVariable("total_price", shoppingCart.sumCart());
            context.setVariable("number_of_items", shoppingCart.getNumberOfItems());
            context.setVariable("category_list", productCategoryDataStore.getAll());
            context.setVariable("supplier_list", supplierDataStore.getAll());
            context.setVariable("category", category);
            context.setVariable("supplier", supplier);
            context.setVariable("products", products);

            engine.process("product/index.html", context, resp.getWriter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String productId = request.getParameter("id");
        ShoppingCartDaoDB shoppingCart = getShoppingCart(request);

        shoppingCart.addItem(Integer.parseInt(productId));

        float priceSum = shoppingCart.sumCart();
        int numberOfItems = shoppingCart.getNumberOfItems();

        JSONObject json = new JSONObject();
        json.put("priceSum", priceSum);
        json.put("numberOfItems", numberOfItems);

        response.setContentType("application/json");
        response.getWriter().print(json);
    }

    private ShoppingCartDaoDB getShoppingCart(HttpServletRequest request) {
        HttpSession session;
        session = request.getSession();
        if (session.isNew()) {
            session.setAttribute("UserObject", new User());
        }
        User user = (User) session.getAttribute("UserObject");
        return user.shoppingCart;
    }

}
