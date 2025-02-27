package com.course.web.rest.wallet;


import com.course.common.utils.ResponseUtils;
import com.course.config.properties.SepayProperties;
import com.course.dto.intergration.CheckTransaction;
import com.course.dto.intergration.Transaction;
import com.course.dto.intergration.TransactionSepayResponse;
import com.course.service.WalletService;
import com.course.service.impl.WalletServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/hooks/sepay-payment/*")
public class WebhookWalletApi extends HttpServlet {

    private Gson gson;

    private WalletService walletService;

    private static final SepayProperties sepayProperties = SepayProperties.getInstance();


    @Override
    public void init(ServletConfig config) throws ServletException {
        gson = getBean(Gson.class.getSimpleName());
        walletService = getBean(WalletServiceImpl.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        Long tranId = null;
        if (pathInfo != null && pathInfo.length() > 1) {
            String tranIdSTr = pathInfo.substring(1);
            try{
                tranId = Long.parseLong(tranIdSTr);
            }
            catch (Exception e){
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        gson.toJson("Không tìm thấy"));
            }
        }
        try {
            String jsonResponse = HttpPaymentUtils.get(sepayProperties.getUrl() + "/list?limit=10", sepayProperties.getApiToken());
            TransactionSepayResponse responseObj = gson.fromJson(jsonResponse, TransactionSepayResponse.class);
            if (responseObj != null && responseObj.getTransactions() != null) {
                List<Transaction> transactions = responseObj.getTransactions();
                CheckTransaction checkTransaction = walletService.checkTransaction(transactions.get(0), tranId);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK,
                        gson.toJson(checkTransaction));
            } else {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK,
                        gson.toJson(CheckTransaction.builder().success(false).build()));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi server"));
        }
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//        try{
//            WebhooksRequest webhooksRequest = gson.fromJson(req.getReader(), WebhooksRequest.class);
//            walletService.authenticateTransactional(webhooksRequest);
//            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson("Success"));
//        }catch (ForbiddenException e){
//            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(e.getMessage()));
//        }
//        catch (Exception e){
//            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Lỗi server"));
//        }
//    }
}
