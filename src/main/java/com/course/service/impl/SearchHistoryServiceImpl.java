package com.course.service.impl;

import com.course.core.bean.annotations.Service;
import com.course.dao.AccountDAO;
import com.course.dao.SearchHistoryDAO;
import com.course.entity.AccountEntity;
import com.course.entity.SearchHistoryEntity;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final SearchHistoryDAO searchHistoryDAO;
    private final AccountDAO accountDAO;


    @Override
    public void saveSearch(String content) {
        AccountEntity account = getAuthenticatedAccount();

        SearchHistoryEntity searchHistory = new SearchHistoryEntity();
        searchHistory.setAccount(account);
        searchHistory.setContent(content);
        searchHistory.setCreatedAt(LocalDateTime.now());
        searchHistoryDAO.save(searchHistory);
    }

    @Override
    public List<String> getRecentSearches(int limit) {
        AccountEntity account = getAuthenticatedAccount();
        if(account == null) {
            return List.of();
        }
        return searchHistoryDAO.getRecentSearches(account.getEmail(), limit);
    }

    private AccountEntity getAuthenticatedAccount() {
        if(AuthenticationContextHolder.getContext() == null) {
            return null;
        }
        String email = AuthenticationContextHolder.getContext().getEmail();
        return accountDAO.findByEmail(email);
    }
}


