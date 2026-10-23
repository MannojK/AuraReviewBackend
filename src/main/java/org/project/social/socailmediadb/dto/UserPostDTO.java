package org.project.social.socailmediadb.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserPostDTO {
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;

    // Constructors, Getters, Setters
}