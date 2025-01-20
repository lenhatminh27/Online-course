package com.course.service.impl;

import com.course.common.utils.StringUtils;
import com.course.dao.AccountDAO;
import com.course.dao.BlogDAO;
import com.course.dao.TagDAO;
import com.course.dto.request.BlogCreateRequest;
import com.course.dto.response.AccountResponse;
import com.course.dto.response.BlogResponse;
import com.course.dto.response.TagResponse;
import com.course.entity.AccountEntity;
import com.course.entity.BlogEntity;
import com.course.entity.TagEntity;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.BlogService;

import java.time.LocalDateTime;
import java.util.List;

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

        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountEntity = accountDAO.findByEmail(email);

        String slug = StringUtils.toSlug(blogCreateRequest.getTitle()) + "-" + accountEntity.getId();

        List<TagEntity> existingTags = tagDAO.findAllByTagName(blogCreateRequest.getTagName());
        // Kiểm tra xem những tagName nào đã tồn tại cho vào list
        List<String> existingTagNames = existingTags.stream()
                .map(TagEntity::getName)
                .toList();

        // Lấy những tagName mới vao list
        List<String> newTagNames = blogCreateRequest.getTagName().stream()
                .filter(tagName -> !existingTagNames.contains(tagName))
                .toList();

        // Tạo TagEntity cho những tag name mới
        List<TagEntity> newTags = newTagNames.stream()
                .map(tagName -> {
                    TagEntity tagEntity = new TagEntity();
                    tagEntity.setName(tagName);
                    return tagEntity;
                })
                .toList();

        if (!newTags.isEmpty()) {
            tagDAO.saveAll(newTags);
        }

        List<TagEntity> allTags = new java.util.ArrayList<>();
        allTags.addAll(existingTags);
        allTags.addAll(newTags);

        // Tạo blogEntity
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setTitle(blogCreateRequest.getTitle());
        blogEntity.setSlug(slug);
        blogEntity.setAccount(accountEntity);
        blogEntity.setTags(allTags);
        blogEntity.setContent(blogCreateRequest.getContent());
        blogEntity.setCreateBy(email);
        blogEntity.setUpdatedAt(LocalDateTime.now());
        BlogEntity savedBlog = blogDAO.createBlog(blogEntity);

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setEmail(email);
        accountResponse.setAvatar(accountEntity.getAvatar());

        List<TagResponse> tagResponses = allTags.stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .toList();

        BlogResponse blogResponse = new BlogResponse();
        blogResponse.setId(savedBlog.getId());
        blogResponse.setTitle(savedBlog.getTitle());
        blogResponse.setSlug(savedBlog.getSlug());
        blogResponse.setContent(savedBlog.getContent());
        blogResponse.setCreateAt(savedBlog.getCreateAt().toString());
        blogResponse.setTagResponses(tagResponses);
        blogResponse.setAccountResponse(accountResponse);

        return blogResponse;
    }

    @Override
    public boolean existTitle(String title) {
        return blogDAO.existTitle(title);
    }
}
