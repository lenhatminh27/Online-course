package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dto.response.AccountResponse;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.AccountService;
import com.course.service.impl.AccountServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/accounts")
public class AccountApi extends BaseServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private Gson gson;

    private AccountService accountService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.accountService = getBean(AccountServiceImpl.class.getSimpleName());
        gson = getBean(Gson.class.getSimpleName());
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
