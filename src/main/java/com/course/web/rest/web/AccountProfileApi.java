package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.AccountProfileDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dao.impl.AccountProfileDAOImpl;
import com.course.dto.request.AccountProfileRequest;
import com.course.dto.response.AccountProfileResponse;
import com.course.dto.response.ErrorResponse;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.AccountProfileService;
import com.course.service.impl.AccountProfileServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/account-profile/*")
public class AccountProfileApi extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    private final AccountProfileService accountProfileService;

    public AccountProfileApi() {
        AccountProfileDAO accountProfileDAO = new AccountProfileDAOImpl();
        AccountDAO accountDAO = new AccountDaoImpl();
        accountProfileService = new AccountProfileServiceImpl(accountProfileDAO, accountDAO);
    }

    @Override
    @IsAuthenticated
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            AccountProfileResponse accountProfileResponse = accountProfileService.getAccountProfileByAccount();
            gson.toJson(accountProfileResponse);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(accountProfileResponse));
        } catch (Exception e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "sever error");
        }
    }

    @Override
    @IsAuthenticated
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            AccountProfileRequest body = gson.fromJson(req.getReader(), AccountProfileRequest.class);
            List<String> errors = new ArrayList<>();
            if (ObjectUtils.isEmpty(body.getLastName())) {
                errors.add("Họ phải được yêu cầu");
            }
            if (ObjectUtils.isEmpty(body.getFirstName())) {
                errors.add("Tên phải được yêu cầu");
            }
            if (errors.size() > 0) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setError(errors);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }
            // Gọi service để cập nhật thông tin tài khoản
            AccountProfileResponse updatedProfile = accountProfileService.updateAccountProfile(body);

            // Trả về thông tin tài khoản đã cập nhật với mã 200 (OK)
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(updatedProfile));
        } catch (Exception e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "sever error");
        }
    }
}
