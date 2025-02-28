package com.course.web.rest.web;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.CategoryDAO;
import com.course.dto.request.BookmarksBlogRequest;
import com.course.dto.request.CategoryCreateRequest;
import com.course.dto.request.UpdateCategoryRequest;
import com.course.dto.response.CategoryResponse;
import com.course.dto.response.ErrorResponse;
import com.course.exceptions.NotFoundException;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

    @IsAuthenticated
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.equals("/no-parent")) {
            handleGetAllNoParent(resp);
            return;
        } else {
            try {
                List<CategoryResponse> categoryResponses = categoryService.getAllCategories();
                if (categoryResponses.isEmpty()) {
                    ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, gson.toJson("Không có category nào tồn tại"));
                    return;
                }
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(categoryResponses));
            } catch (Exception e) {
                e.printStackTrace();
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Server Error"));
            }
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
        if(ObjectUtils.isEmpty(categoryCreateRequest)){
            errors.add("CategoryCreateRequest không được rỗng");
        }
        if(ObjectUtils.isEmpty(categoryCreateRequest.getName())){
            errors.add("CategoryName không được rỗng");
        }
        if (categoryCreateRequest.getName().length() > 100 || categoryCreateRequest.getName().length() < 3) {
            errors.add("Tên thể loại phải từ 3-100 kí tự");
        }
        if (categoryService.isExistCategory(categoryCreateRequest)) {
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
            CategoryResponse categoryResponse = categoryService.createCategory(categoryCreateRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_CREATED, gson.toJson(categoryResponse));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }
    @IsAuthenticated
    @HasPermission("UPDATE_CATEGORY")
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            UpdateCategoryRequest updateCategoryRequest = gson.fromJson(req.getReader(), UpdateCategoryRequest.class);
            List<String> errors = new ArrayList<>();
            if(ObjectUtils.isEmpty(updateCategoryRequest)){
                errors.add("UpdateCategoryRequest không được rỗng");
            }
            if(ObjectUtils.isEmpty(updateCategoryRequest.getName())){
                errors.add("CategoryName không được rỗng");
            }
            if (updateCategoryRequest.getName().length() > 100 || updateCategoryRequest.getName().length() < 3) {
                errors.add("Tên thể loại phải từ 3-100 kí tự");
            }
            if (categoryService.isDuplicateCategory(updateCategoryRequest)) {
                errors.add("Thể loại này đã tồn tại");
            }
            if (!Pattern.matches(CATEGORY_NAME_REGEX, updateCategoryRequest.getName())) {
                errors.add("Tên thể loại chỉ được chứa chữ cái, số, khoảng trắng, dấu gạch ngang (-) hoặc gạch dưới (_)");
            }
            if (errors.size() > 0) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setError(errors);
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
                return;
            }
            CategoryResponse categoryResponse = categoryService.updateCategory(updateCategoryRequest);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(categoryResponse));
        } catch (Exception e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
        }
    }

    @IsAuthenticated
    @HasPermission("DELETE_CATEGORY")
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        UpdateCategoryRequest categoryRequest = gson.fromJson(req.getReader(), UpdateCategoryRequest.class);
        if (ObjectUtils.isEmpty(categoryRequest)) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(List.of("Category không được null"));
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(errorResponse));
            return;
        }

        try {
            Long categoryId = categoryRequest.getCategoryId();
            categoryService.deleteCategory(categoryId);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, "");
        } catch (NumberFormatException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("CategoryId không hợp lệ"));
        } catch (NotFoundException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, gson.toJson("Category không tồn tại"));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Server Error"));
        }
    }


}
