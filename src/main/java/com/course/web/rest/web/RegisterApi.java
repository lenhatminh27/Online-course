package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.RoleDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dao.impl.RoleDAOImpl;
import com.course.dto.request.RegisterRequest;
import com.course.dto.response.ErrorResponse;
import com.course.service.AccountService;
import com.course.service.impl.AccountServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/register")
public class RegisterApi extends HttpServlet {

    private static final long serialVersionUID = 1L;


    private final AccountService accountService;

    public RegisterApi() {
        AccountDAO accountDAO = new AccountDaoImpl();
        RoleDAO roleDAO = new RoleDAOImpl();
        this.accountService = new AccountServiceImpl(accountDAO, roleDAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("common/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        AccountDAO accountDAO = new AccountDaoImpl();
        try {
            RegisterRequest registerRequest = gson.fromJson(req.getReader(), RegisterRequest.class);
            List<String> errors = new ArrayList<>();
            if (ObjectUtils.isEmpty(registerRequest)) {
                errors.add("register request is empty");
            }
            if (!registerRequest.getConfirmPassword().equals(registerRequest.getPassword())) {
                errors.add("Passwords do not match");
            }
            if (ObjectUtils.isEmpty(registerRequest.getFirstName())) {
                errors.add("First name is empty");
            }
            if (ObjectUtils.isEmpty(registerRequest.getLastName())) {
                errors.add("Last name is empty");
            }
            if (ObjectUtils.isEmpty(registerRequest.getEmail())) {
                errors.add("email is empty");
            }
            if (ObjectUtils.isEmpty(registerRequest.getPassword())) {
                errors.add("password is empty");
            }

            if (!accountDAO.isValidEmail(registerRequest.getEmail())) {
                List<String> error = new ArrayList<>();
                error.add("Email không hợp lệ!");
                ErrorResponse errorResponse = new ErrorResponse(error);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }

            if (!registerRequest.getConfirmPassword().equals(registerRequest.getPassword())) {
                List<String> error = new ArrayList<>();
                error.add("Mật khẩu không trùng khớp!");
                ErrorResponse errorResponse = new ErrorResponse(error);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }

            if (errors.size() > 0) {
                ErrorResponse errorResponse = new ErrorResponse(errors);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }
            accountService.registerAccount(registerRequest, resp);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_CREATED, gson.toJson("Đăng ký thành công!"));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Có lỗi xảy ra!"));
        }
    }
}
