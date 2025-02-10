package com.course.dao;

import com.course.entity.SearchHistoryEntity;

import java.util.List;

public interface SearchHistoryDAO {
    void save(SearchHistoryEntity searchHistory);

    List<String> getRecentSearches(String email, int limit);
}