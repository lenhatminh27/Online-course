package com.course.web.rest;

import com.course.Product;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/api/product")
public class ProductApi extends HttpServlet {

    private final Gson gson = new Gson(); // Gson instance dùng chung

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            Product product = gson.fromJson(req.getReader(), Product.class);
            if (product.getId() < 0) {
                throw new Exception("manh");
            }
            System.out.println("Received Product: " + product);
            writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(product));
        } catch (JsonSyntaxException e) {
            // Xử lý lỗi JSON không hợp lệ
            writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Invalid JSON format\"}");
        } catch (Exception e) {
            // Xử lý lỗi khác
            writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"Server error\"}");
            e.printStackTrace();
        }
    }

    // Hàm hỗ trợ gửi phản hồi JSON
    private void writeResponse(HttpServletResponse resp, int status, String jsonResponse) throws IOException {
        resp.setStatus(status);
        try (PrintWriter out = resp.getWriter()) {
            out.write(jsonResponse);
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
