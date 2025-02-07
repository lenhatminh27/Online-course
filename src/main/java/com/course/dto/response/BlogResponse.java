package com.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String slug;

    private String content;

    private boolean isLike;

    private Long likesCount;

    private Long viewsCount;

    private Long commentsCount;

    private boolean isBookmark;

    private AccountResponse accountResponse;

    private List<TagResponse> tagResponses;

    private String createAt;

    public BlogResponse(Long id, String title, String slug, String content, AccountResponse accountResponse, List<TagResponse> tagResponses, String createAt) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.content = content;
        this.accountResponse = accountResponse;
        this.tagResponses = tagResponses;
        this.createAt = createAt;
    }

    public void setIsBookmark(boolean b) {
        this.isBookmark = b;
    }

    public void setIsLike(boolean b) {
        this.isLike = b;
    }
}

