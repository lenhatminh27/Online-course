package com.course.service.impl;

import com.course.common.utils.StringUtils;
import com.course.dao.AccountDAO;
import com.course.dao.BlogDAO;
import com.course.dao.TagDAO;
import com.course.dto.request.BlogCreateRequest;
import com.course.dto.request.BlogFilterRequest;
import com.course.dto.response.AccountResponse;
import com.course.dto.response.BlogResponse;
import com.course.dto.response.PageResponse;
import com.course.dto.response.TagResponse;
import com.course.entity.AccountEntity;
import com.course.entity.BlogEntity;
import com.course.entity.TagEntity;
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

    public BlogServiceImpl(BlogDAO blogDAO, AccountDAO accountDAO, TagDAO tagDAO) {
        this.blogDAO = blogDAO;
        this.accountDAO = accountDAO;
        this.tagDAO = tagDAO;
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

        return convertToBlogResponse(blogDAO.createBlog(blogEntity));
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

    private BlogResponse convertToBlogResponse(BlogEntity blogEntity) {
        List<TagResponse> tagResponses = blogEntity.getTags().stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .toList();

        AccountEntity author = blogEntity.getAccount();
        AccountResponse accountResponse = new AccountResponse(author.getEmail(), author.getAvatar());

        return new BlogResponse(
                blogEntity.getId(),
                blogEntity.getTitle(),
                blogEntity.getSlug(),
                blogEntity.getContent(),
                accountResponse,
                tagResponses,
                blogEntity.getCreateAt().toString()
        );
    }
}

