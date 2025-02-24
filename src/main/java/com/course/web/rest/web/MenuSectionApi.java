package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dto.response.MenuSectionResponse;
import com.course.dto.response.SectionResponse;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.MenuSectionService;
import com.course.service.SectionService;
import com.course.service.impl.MenuSectionServiceImpl;
import com.course.service.impl.SectionServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.List;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@WebServlet("/api/menu-section/*")
public class MenuSectionApi extends BaseServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private Gson gson;

    private MenuSectionService menuSectionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        gson = getBean(Gson.class.getSimpleName());
        menuSectionService = getBean(MenuSectionServiceImpl.class.getSimpleName());
    }

    @Override
    @IsAuthenticated
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            String courseIdStr = pathInfo.substring(1);
            try {
                Long courseId = Long.parseLong(courseIdStr);
                handleGetMenuSectionsByCourse(courseId, resp);
            } catch (NumberFormatException e) {
                ResponseUtils.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, gson.toJson("Invalid number"));
            }
            return;
        }
    }

    private void handleGetMenuSectionsByCourse(Long courseId, HttpServletResponse resp) throws IOException {
        try{
            List<MenuSectionResponse> response = menuSectionService.getMenuSectionByCourseId(courseId);
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(response));
        }
        catch (ForbiddenException e) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_FORBIDDEN,  gson.toJson(e.getMessage()));
        }
        catch (NotFoundException e){
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,  gson.toJson(e.getMessage()));
        }
    }
}
