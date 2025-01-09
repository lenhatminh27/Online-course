package com.course.web.rest;

import com.course.Product;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/test")
public class TestApi extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Tạo danh sách sản phẩm giả lập
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Laptop", 1500.0));
        products.add(new Product(2, "Smartphone", 800.0));
        products.add(new Product(3, "Tablet", 500.0));
        // Chuyển đổi danh sách sản phẩm sang JSON
        Gson gson = new Gson();
        String json = gson.toJson(products);
        // Thiết lập kiểu nội dung và trả về JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write(json);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
