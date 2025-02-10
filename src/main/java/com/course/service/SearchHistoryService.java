package com.course.service;

import java.util.List;

public interface SearchHistoryService {
    void saveSearch(String content);
    List<String> getRecentSearches(int limit);
}
