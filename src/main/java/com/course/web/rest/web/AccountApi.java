package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.RoleDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dao.impl.RoleDAOImpl;
import com.course.dto.response.AccountResponse;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.AccountService;
import com.course.service.impl.AccountServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/api/accounts")
public class AccountApi extends BaseServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private final transient Gson gson = new Gson();

    private final AccountService accountService;

    public AccountApi() {
        AccountDAO accountDao = new AccountDaoImpl();
        RoleDAO roleDAO = new RoleDAOImpl();
        this.accountService = new AccountServiceImpl(accountDao, roleDAO);
    }


    @Override
    @IsAuthenticated()
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        AccountResponse currentAccount = this.accountService.getCurrentAccount();
        ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(currentAccount));
    }
}
