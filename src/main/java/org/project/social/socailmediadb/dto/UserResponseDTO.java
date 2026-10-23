package org.project.social.socailmediadb.dto;

import lombok.Getter;
import lombok.Setter;
import org.project.social.socailmediadb.entity.Post;

import java.util.List;

//@Getter
//@Setter
public class UserResponseDTO {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public List<PostResponseDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponseDTO> posts) {
        this.posts = posts;
    }

    private Long id;
    private String username;
    private int postCount;
    private int followerCount;
    private int followingCount;
    private List<PostResponseDTO> posts;

    // Standard Constructors, Getters, and Setters
}

