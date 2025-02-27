package com.course.web.rest.wallet;

import com.course.common.utils.ResponseUtils;
import com.course.dto.intergration.WalletResponse;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.WalletService;
import com.course.service.impl.WalletServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/wallets")
public class WalletApi extends BaseServlet {


    private Gson gson;

    private WalletService walletService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        gson = getBean(Gson.class.getSimpleName());
        walletService = getBean(WalletServiceImpl.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try{
            WalletResponse wallet = walletService.getWallet();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(wallet));
        }catch (Exception e){
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
