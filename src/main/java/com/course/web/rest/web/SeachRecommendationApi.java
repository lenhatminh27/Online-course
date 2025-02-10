package com.course.web.rest.web;

import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.SearchHistoryDAO;
import com.course.dao.impl.AccountDaoImpl;
import com.course.dao.impl.SearchHistoryDAOImpl;
import com.course.dto.response.SearchRecommendationResponse;
import com.course.security.annotations.IsAuthenticated;
import com.course.security.annotations.handle.BaseServlet;
import com.course.service.SearchHistoryService;
import com.course.service.impl.SearchHistoryServiceImpl;
import com.google.gson.Gson;
import com.google.protobuf.Api;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/search-recommendation")
public class SeachRecommendationApi extends BaseServlet {


    private final Gson gson = new Gson();
    private final SearchHistoryService searchHistoryService;

    public SeachRecommendationApi() {
        SearchHistoryDAO searchHistoryDAO = new SearchHistoryDAOImpl();
        AccountDAO accountDAO = new AccountDaoImpl();
        this.searchHistoryService = new SearchHistoryServiceImpl(searchHistoryDAO, accountDAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            int limit = 5;
            SearchRecommendationResponse searchRecommendationResponse = new SearchRecommendationResponse(searchHistoryService.getRecentSearches(limit));
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_OK, gson.toJson(searchRecommendationResponse));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi server");
        }
    }
}
