package com.course.service.impl;

import com.course.dao.AccountDAO;
import com.course.dao.BlogDAO;
import com.course.dao.BookmarksBlogDAO;
import com.course.dto.request.BookmarksBlogRequest;
import com.course.dto.response.BookmarksBlogResponse;
import com.course.entity.AccountEntity;
import com.course.entity.BlogEntity;
import com.course.entity.BookmarksBlogEntity;
import com.course.exceptions.NotFoundException;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.BookmarksBlogService;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


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
        BlogEntity blog = blogDAO.findBlogById(bookmarksBlogRequest.getBlogId());
        if (blog == null) {
            throw new NotFoundException("Bài viết không tồn tại!");
        }
        BookmarksBlogEntity newBookmark = new BookmarksBlogEntity();
        newBookmark.setAccount(accountCurrent);
        newBookmark.setBlog(blog);
        bookmarksBlogDAO.save(newBookmark);
    }

    @Override
    //@Transactional giúp duy trì session Hibernate suốt quá trình truy vấn, tránh lỗi LazyInitializationException
    @Transactional
    public List<BookmarksBlogResponse> getBookmarkedBlogs() {
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountCurrent = accountDAO.findByEmail(email);
        List<BookmarksBlogEntity> bookmarks = bookmarksBlogDAO.getBookmarkedBlogsByUserId(accountCurrent.getId());
        // Định dạng ngày giờ thành chuỗi
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return bookmarks.stream().map(bookmark -> {
            BlogEntity blog = bookmark.getBlog();
            return new BookmarksBlogResponse(blog.getId(),
                    blog.getTitle(), blog.getSlug(),
                    blog.getAccount().getEmail(), blog.getCreateAt().format(formatter)
            );
        }).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteBookmark(Long blogId) throws NotFoundException {
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountCurrent = accountDAO.findByEmail(email);

        BookmarksBlogEntity bookmark = bookmarksBlogDAO.findByBlogIdAndUserId(blogId, accountCurrent.getId());
        if (bookmark == null) {
            throw new NotFoundException("Bookmark không tồn tại");
        }

        bookmarksBlogDAO.deleteBookmarksBlogByBlogIdAndUserId(blogId, accountCurrent.getId());
    }
}