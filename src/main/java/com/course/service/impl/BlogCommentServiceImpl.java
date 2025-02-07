package com.course.service.impl;

import com.course.dao.AccountDAO;
import com.course.dao.BlogCommentDAO;
import com.course.dao.BlogDAO;
import com.course.dto.request.BlogCommentCreateRequest;
import com.course.dto.request.BlogCommentUpdateRequest;
import com.course.dto.response.AccountResponse;
import com.course.dto.response.BlogCommentResponse;
import com.course.entity.AccountEntity;
import com.course.entity.BlogCommentEntity;
import com.course.entity.BlogEntity;
import com.course.exceptions.ForbiddenException;
import com.course.exceptions.NotFoundException;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.BlogCommentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BlogCommentServiceImpl implements BlogCommentService {

    private final BlogDAO blogDAO;

    private final AccountDAO accountDAO;

    private final BlogCommentDAO blogCommentDAO;

    public BlogCommentServiceImpl(BlogDAO blogDAO, AccountDAO accountDAO, BlogCommentDAO blogCommentDAO) {
        this.blogDAO = blogDAO;
        this.accountDAO = accountDAO;
        this.blogCommentDAO = blogCommentDAO;
    }
    @Override
    public BlogCommentResponse createBlogComment(BlogCommentCreateRequest blogCommentCreateRequest) {
        String email = AuthenticationContextHolder.getContext().getEmail();
        AccountEntity accountEntity = accountDAO.findByEmail(email);
        BlogEntity blogEntity = blogDAO.findBlogById(blogCommentCreateRequest.getBlogId());
        BlogCommentEntity parentComment = null;
        if (blogCommentCreateRequest.getParentId() != null) {
            parentComment = blogCommentDAO.findBlogCommentById(blogCommentCreateRequest.getParentId());
        }
        BlogCommentEntity blogCommentEntity = new BlogCommentEntity();
        blogCommentEntity.setBlog(blogEntity);
        blogCommentEntity.setAccount(accountEntity);
        blogCommentEntity.setContent(blogCommentCreateRequest.getContent());
        blogCommentEntity.setParentComment(parentComment);
        blogCommentEntity.setCreateAt(LocalDateTime.now());
        BlogCommentEntity blogCommentSave = blogCommentDAO.createBlogComment(blogCommentEntity);
        return convertToBlogCommentResponse(blogCommentSave);
    }

    @Override
    public BlogCommentResponse updateBlogComment(BlogCommentUpdateRequest blogCommentUpdateRequest) {
        AccountEntity account = accountDAO.findByEmail(AuthenticationContextHolder.getContext().getEmail());
        BlogCommentEntity blogComment = blogCommentDAO.findBlogCommentById(blogCommentUpdateRequest.getId());
        if (!account.getId().equals(blogComment.getAccount().getId())) {
            throw new ForbiddenException("Bạn không có quyền cập nhật comment này");
        }
        blogComment.setContent(blogCommentUpdateRequest.getContent());
        blogCommentDAO.updateBlogComment(blogComment);
        return convertToBlogCommentResponse(blogComment);
    }

    @Override
    public List<BlogCommentResponse> getListCommentByBlogSlug(String slug) {
        BlogEntity blogEntity = blogDAO.findBlogBySlug(slug);
        if (blogEntity == null) {
            return new ArrayList<>();
        }
        List<BlogCommentEntity> list = blogCommentDAO.findListCommentByBlogSlug(slug);
        List<BlogCommentResponse> responseList = new ArrayList<>();
        for (BlogCommentEntity blogCommentEntity : list) {
            responseList.add(convertToBlogCommentResponse(blogCommentEntity));
        }
        return responseList;
    }

    private BlogCommentResponse convertToBlogCommentResponse(BlogCommentEntity blogCommentEntity) {
        AccountResponse accountResponse = convertToAccountResponse(blogCommentEntity.getAccount());
        List<BlogCommentEntity> listChildren = blogCommentDAO.findAllChildrenBlogComments(blogCommentEntity.getId());
        List<BlogCommentResponse> blogCommentResponseList = new ArrayList<>();
        if (listChildren != null) {
            for (BlogCommentEntity child : listChildren) {
                blogCommentResponseList.add(convertToBlogCommentResponse(child));
            }
        }
        return new BlogCommentResponse(
                blogCommentEntity.getId(),
                blogCommentEntity.getBlog().getId(),
                accountResponse,
                blogCommentEntity.getContent(),
                blogCommentResponseList,
                blogCommentEntity.getCreateAt().toString(),
                blogCommentEntity.getUpdatedAt() != null ? blogCommentEntity.getUpdatedAt().toString() : null
        );
    }

    private AccountResponse convertToAccountResponse(AccountEntity accountEntity) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setEmail(accountEntity.getEmail());
        accountResponse.setAvatar(accountEntity.getAvatar());
        return accountResponse;
    }


}
