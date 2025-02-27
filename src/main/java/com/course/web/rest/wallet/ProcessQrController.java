package com.course.web.rest.wallet;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dto.request.ProcessQrRequest;
import com.course.dto.response.ErrorResponse;
import com.course.dto.intergration.ProcessQrResponse;
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
import java.util.ArrayList;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/process/qr")
public class ProcessQrController extends BaseServlet {

    private Gson gson;

    private WalletService walletService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        gson = getBean(Gson.class.getSimpleName());
        walletService = getBean(WalletServiceImpl.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try{
            List<String> errors = new ArrayList<>();
            ProcessQrRequest processQrRequest = gson.fromJson(req.getReader(), ProcessQrRequest.class);
            if (ObjectUtils.isEmpty(processQrRequest)){
                errors.add("Request không được để trống");
            }
            if(ObjectUtils.isEmpty(processQrRequest.getPoint())){
                errors.add("Số point là bắt buộc");
            }
            if(!ObjectUtils.isEmpty(errors)){
                ErrorResponse errorResponse = new ErrorResponse(errors);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }
            ProcessQrResponse processQrResponse = walletService.generateQr(processQrRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(processQrResponse));
        }catch (Exception e){
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
