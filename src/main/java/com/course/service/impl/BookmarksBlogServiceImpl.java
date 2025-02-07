package com.course.service.impl;

import com.course.dao.AccountDAO;
import com.course.dao.BlogDAO;
import com.course.dao.BookmarksBlogDAO;
import com.course.dto.request.BookmarksBlogRequest;
import com.course.entity.AccountEntity;
import com.course.entity.BlogEntity;
import com.course.entity.BookmarksBlogEntity;
import com.course.exceptions.NotFoundException;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.BookmarksBlogService;

public class BookmarksBlogServiceImpl implements BookmarksBlogService {

    private final BookmarksBlogDAO bookmarksBlogDAO;
    private final BlogDAO blogDAO;
    private final AccountDAO accountDAO;

    public BookmarksBlogServiceImpl(BookmarksBlogDAO bookmarksBlogDAO, BlogDAO blogDAO, AccountDAO accountDAO) {
        this.bookmarksBlogDAO = bookmarksBlogDAO;
        this.blogDAO = blogDAO;
        this.accountDAO = accountDAO;
    }

    @Override
    public void createBookmarksBlog(BookmarksBlogRequest bookmarksBlogRequest) {
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountCurrent = accountDAO.findByEmail(email);

        if (bookmarksBlogDAO.existsBookmarkBlogId(bookmarksBlogRequest.getBlogId(), accountCurrent.getId())) {
            throw new NotFoundException("Không tìm thấy bài viết có id = " + bookmarksBlogRequest.getBlogId() + "!");
        }

        BlogEntity blog = blogDAO.getBlogByBlogId(bookmarksBlogRequest.getBlogId());
        if (blog == null) {
            throw new NotFoundException("Bài viết không tồn tại!");
        }

        BookmarksBlogEntity newBookmark = new BookmarksBlogEntity();
        newBookmark.setAccount(accountCurrent);
        newBookmark.setBlog(blog);
        bookmarksBlogDAO.save(newBookmark);

        // Trả về response
    }
}


