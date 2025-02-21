package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.CategoryDAO;
import com.course.dto.request.CategoryCreateRequest;
import com.course.dto.response.BlogCommentResponse;
import com.course.dto.response.CategoryResponse;
import com.course.dto.response.ErrorResponse;
import com.course.security.annotations.HasPermission;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.CategoryService;
import com.course.service.impl.CategoryServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/category/*")
public class CategoryApi extends BaseServlet {

    private Gson gson;
    private CategoryService categoryService;
    private CategoryDAO categoryDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        gson = getBean(Gson.class.getSimpleName());
        categoryService = getBean(CategoryServiceImpl.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.equals("/no-parent")) {
            handleGetAllNoParent(resp);
            return;
        }
    }

    private void handleGetAllNoParent(HttpServletResponse resp) throws IOException {
        try {
            List<CategoryResponse> res = categoryService.getAllCategoriesParent();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(res));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }

    private static final String CATEGORY_NAME_REGEX = "^[a-zA-ZÀ-ỹ0-9_\\-\\s]+$";
    @Override
    @IsAuthenticated
    @HasPermission("CREATE_CATEGORY")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        CategoryCreateRequest categoryCreateRequest = gson.fromJson(req.getReader(), CategoryCreateRequest.class);
        List<String> errors = new ArrayList<>();
        CategoryResponse categoryResponse = categoryService.createCategory(categoryCreateRequest);
        if(ObjectUtils.isEmpty(categoryCreateRequest)){
            errors.add("CategoryCreateRequest không được rỗng");
        }
        if(ObjectUtils.isEmpty(categoryCreateRequest.getName())){
            errors.add("CategoryName không được rỗng");
        }
        if (categoryCreateRequest.getName().length() > 100 || categoryCreateRequest.getName().length() < 3) {
            errors.add("Tên thể loại phải từ 3-100 kí tự");
        }
        if (categoryResponse == null) {
            errors.add("Thể loại này đã tồn tại");
        }
        if (!Pattern.matches(CATEGORY_NAME_REGEX, categoryCreateRequest.getName())) {
            errors.add("Tên thể loại chỉ được chứa chữ cái, số, khoảng trắng, dấu gạch ngang (-) hoặc gạch dưới (_)");
        }
        if (!ObjectUtils.isEmpty(errors)) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(errors);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }
        try {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_CREATED, gson.toJson(categoryResponse));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }
}
