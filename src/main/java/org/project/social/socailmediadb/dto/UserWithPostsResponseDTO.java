package org.project.social.socailmediadb.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// UserWithPostsResponseDTO.java
@Getter
@Setter
public class UserWithPostsResponseDTO {
    private Long userId;
    private String username;
    private int followerCount;
    private int followingCount;
    private List<UserPostDTO> posts;

    // Constructors, Getters, Setters
}