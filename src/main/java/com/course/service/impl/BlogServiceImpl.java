package com.course.service.impl;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.StringUtils;
import com.course.dao.*;
import com.course.dto.request.BlogCreateRequest;
import com.course.dto.request.BlogFilterRequest;
import com.course.dto.request.BlogUpdateRequest;
import com.course.dto.response.AccountResponse;
import com.course.dto.response.BlogResponse;
import com.course.dto.response.PageResponse;
import com.course.dto.response.TagResponse;
import com.course.entity.AccountEntity;
import com.course.entity.BlogEntity;
import com.course.entity.BlogStatisticEntity;
import com.course.entity.TagEntity;
import com.course.exceptions.AuthenticationException;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.AuthoritiesConstants;
import com.course.security.context.AuthenticationContext;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.BlogService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BlogServiceImpl implements BlogService {

    private final BlogDAO blogDAO;
    private final AccountDAO accountDAO;
    private final TagDAO tagDAO;
    private final BlogStatisticDAO blogStatisticDAO;
    private final BlogCommentDAO blogCommentDAO;
    private final BookmarksBlogDAO bookmarksBlogDAO;

    public BlogServiceImpl(BlogDAO blogDAO, AccountDAO accountDAO, TagDAO tagDAO, BlogStatisticDAO blogStatisticDAO, BlogCommentDAO blogCommentDAO, BookmarksBlogDAO bookmarksBlogDAO) {
        this.blogDAO = blogDAO;
        this.accountDAO = accountDAO;
        this.tagDAO = tagDAO;
        this.blogStatisticDAO = blogStatisticDAO;
        this.blogCommentDAO = blogCommentDAO;
        this.bookmarksBlogDAO = bookmarksBlogDAO;
    }

    @Override
    public BlogResponse createBlog(BlogCreateRequest blogCreateRequest) {
        AccountEntity account = getAuthenticatedAccount();
        String slug = generateSlug(blogCreateRequest.getTitle(), account.getId());

        List<TagEntity> tags = getOrCreateTags(blogCreateRequest.getTagName());

        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setTitle(blogCreateRequest.getTitle());
        blogEntity.setSlug(slug);
        blogEntity.setAccount(account);
        blogEntity.setTags(tags);
        blogEntity.setContent(blogCreateRequest.getContent());
        blogEntity.setCreateBy(account.getEmail());
        blogEntity.setUpdatedAt(LocalDateTime.now());
        BlogEntity blogSave = blogDAO.createBlog(blogEntity);
        BlogStatisticEntity bsEntity = new BlogStatisticEntity();
        bsEntity.setBlog(blogSave);
        bsEntity.setLikes(0L);
        bsEntity.setViews(0L);
        this.blogStatisticDAO.createBlogStatistic(bsEntity);
        return convertToBlogResponse(blogSave);
    }

    @Override
    public boolean existTitle(String title) {
        return blogDAO.existTitle(title);
    }

    @Override
    public PageResponse<BlogResponse> getBlogs(BlogFilterRequest filterRequest) {
        PageResponse<BlogEntity> pageResponse = blogDAO.getBlogsByPage(filterRequest);
        List<BlogResponse> blogs = pageResponse.getData().stream()
                .map(this::convertToBlogResponse)
                .toList();

        return new PageResponse<>(pageResponse.getPage(), pageResponse.getTotalPages(), blogs);
    }

    @Override
    public List<BlogResponse> getTopBlogRecent() {
        return blogDAO.getTopBlogsRecent().stream()
                .map(this::convertToBlogResponse)
                .toList();
    }

    @Override
    public void likeBlog(Long blogId) {
        AccountEntity currentAccount = getAuthenticatedAccount();
        BlogStatisticEntity blogStatistic = blogStatisticDAO.findById(blogId);
        if (blogStatistic != null) {
            blogStatistic.setLikes(blogStatistic.getLikes() + 1);
            blogStatistic.setAccounts(List.of(currentAccount));
            blogStatisticDAO.updateBlogStatistic(blogStatistic);
        } else {
            throw new NotFoundException("Không tìm thấy id của bài viết");
        }
    }

    @Override
    public void deleteLikeBlog(Long blogId) {
        AccountEntity currentAccount = getAuthenticatedAccount();
        BlogStatisticEntity blogStatistic = blogStatisticDAO.findById(blogId);
        if (blogStatistic != null) {
                blogStatistic.setLikes(Math.max(0,blogStatistic.getLikes() - 1));
                blogStatistic.getAccounts().remove(currentAccount);
                blogStatistic.setAccounts(null);
                blogStatisticDAO.updateBlogStatistic(blogStatistic);
        } else {
            throw new NotFoundException("Không tìm thấy id của bài viết");
        }
    }

    @Override
    public BlogResponse findBlogBySlug(String slug) {
        BlogEntity blogEntity = blogDAO.findBlogBySlug(slug);
        if (ObjectUtils.isEmpty(blogEntity)) {
            throw new NotFoundException("Không tìm thấy slug của bài viết");
        }
        BlogResponse blogResponse = convertToBlogResponse(blogEntity);
        return blogResponse;
    }

    @Override
    public PageResponse<BlogResponse> getBlogsByInstructor(BlogFilterRequest filterRequest) {
        AuthenticationContext context = AuthenticationContextHolder.getContext();
        if(ObjectUtils.isEmpty(context)) {
            throw new AuthenticationException("Người dùng chưa đăng nhập");
        }
        if(!context.getAuthorities().contains(AuthoritiesConstants.ROLE_INSTRUCTOR)){
            throw new ForbiddenException("Không được phép truy cập");
        }
        AccountEntity accountCurrent = getAuthenticatedAccount();
        filterRequest.setIdOwner(accountCurrent.getId());
        PageResponse<BlogEntity> pageResponse = blogDAO.getBlogsByPage(filterRequest);
        List<BlogResponse> blogs = pageResponse.getData().stream()
                .map(this::convertToBlogResponse)
                .toList();
        return new PageResponse<>(pageResponse.getPage(), pageResponse.getTotalPages(), blogs);
    }

    @Override
    public void viewBlog(Long blogId) {
        BlogStatisticEntity blogStatistic = blogStatisticDAO.findById(blogId);
        if (blogStatistic != null) {
            blogStatistic.setViews(blogStatistic.getViews() + 1);
            blogStatisticDAO.updateBlogStatistic(blogStatistic);
        } else {
            throw new NotFoundException("Không tìm thấy id của bài viết");
        }
    }

    private AccountEntity getAuthenticatedAccount() {
        String email = AuthenticationContextHolder.getContext().getEmail();
        return accountDAO.findByEmail(email);
    }

    private String generateSlug(String title, Long accountId) {
        return StringUtils.toSlug(title) + "-" + accountId;
    }

    private List<TagEntity> getOrCreateTags(List<String> tagNames) {
        List<TagEntity> existingTags = tagDAO.findAllByTagName(tagNames);
        Set<String> existingTagNames = existingTags.stream().map(TagEntity::getName).collect(Collectors.toSet());

        List<TagEntity> newTags = tagNames.stream()
                .filter(tag -> !existingTagNames.contains(tag))
                .map(tagName -> new TagEntity(tagName))
                .toList();

        if (!newTags.isEmpty()) {
            tagDAO.saveAll(newTags);
        }

        existingTags.addAll(newTags);
        return existingTags;
    }

    @Override
    public void updateBlog(Long blogId, BlogUpdateRequest blogUpdateRequest) {
        AccountEntity account = accountDAO.findByEmail(AuthenticationContextHolder.getContext().getEmail());
        String slug = generateSlug(blogUpdateRequest.getTitle(), account.getId());

        List<TagEntity> tags = getOrCreateTags(blogUpdateRequest.getTagName());

        BlogEntity blog = blogDAO.findBlogById(blogId);

        if (blog == null) {
            throw new ForbiddenException("blog không tồn tại.");
        }
        if (!AuthenticationContextHolder.getContext().getAuthorities().contains("ADMIN") && !account.getId().equals(blog.getAccount().getId())) {
            throw new ForbiddenException("Bạn không có quyền cập nhật blog này");
        }
        blog.setSlug(slug);
        blog.setTags(tags);
        blog.setTitle(blogUpdateRequest.getTitle());
        blog.setContent(blogUpdateRequest.getContent());
        blogDAO.updateBlog(blog);
    }

    private BlogResponse convertToBlogResponse(BlogEntity blogEntity) {
        List<TagResponse> tagResponses = blogEntity.getTags().stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .toList();

        AccountEntity author = blogEntity.getAccount();
        AccountResponse accountResponse = new AccountResponse(author.getEmail(), author.getAvatar());

        BlogResponse blogResponse = new BlogResponse(
                blogEntity.getId(),
                blogEntity.getTitle(),
                blogEntity.getSlug(),
                blogEntity.getContent(),
                accountResponse,
                tagResponses,
                blogEntity.getCreateAt().toString()
        );
        BlogStatisticEntity blogStatistic = blogEntity.getBlogStatistic();
        AuthenticationContext context = AuthenticationContextHolder.getContext();
        if (ObjectUtils.isEmpty(context)){
            blogResponse.setIsLike(false);
            blogResponse.setIsBookmark(false);
        }
        else{
            AccountEntity accountCurrent = getAuthenticatedAccount();
            blogResponse.setIsLike(blogStatisticDAO.existsByIdAccount(accountCurrent.getId(), blogEntity.getId()));
            blogResponse.setIsBookmark(bookmarksBlogDAO.existsBookmarkBlogId(blogEntity.getId(), accountCurrent.getId()));
        }
        if (!ObjectUtils.isEmpty(blogStatistic)){
            blogResponse.setLikesCount(blogStatistic.getLikes());
            blogResponse.setViewsCount(blogStatistic.getViews());
        }
        blogResponse.setCommentsCount(blogCommentDAO.findNumberCommentOfBlog(blogEntity.getId()));
        blogResponse.setIsBookmark(false);
        return blogResponse;
    }

}

