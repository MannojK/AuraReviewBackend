package org.project.social.socailmediadb.service;

import jakarta.transaction.Transactional;
import org.project.social.socailmediadb.dto.*;
import org.project.social.socailmediadb.entity.User;
import org.project.social.socailmediadb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    // 1. CREATE
    public UserResponseDTO createUser(UserRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());

        User savedUser = userRepository.save(user);
        return mapToResponseDTO(savedUser);
    }

    // 2. READ ALL
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // 3. READ ONE
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponseDTO(user);
    }

    // 4. UPDATE
    public UserResponseDTO updateUser(Long id, UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.getUsername());

        User updatedUser = userRepository.save(user);
        return mapToResponseDTO(updatedUser);
    }

    // 5. DELETE
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    // 6. FOLLOW RELATIONSHIP LOGIC
    @Transactional
    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new RuntimeException("You cannot follow yourself");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User toFollow = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        follower.getFollowing().add(toFollow);
        userRepository.save(follower);
    }

    // 7. UNFOLLOW RELATIONSHIP LOGIC
    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User toUnfollow = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        follower.getFollowing().remove(toUnfollow);
        userRepository.save(follower);
    }

    // Helper method to convert Entity to clean DTO
    private UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());

        // Safely extract structural size counts
        dto.setPostCount(user.getPosts() != null ? user.getPosts().size() : 0);
        dto.setFollowerCount(user.getFollowers() != null ? user.getFollowers().size() : 0);
        dto.setFollowingCount(user.getFollowing() != null ? user.getFollowing().size() : 0);
        return dto;
    }

    public UserWithPostsResponseDTO getUserWithPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserWithPostsResponseDTO dto = new UserWithPostsResponseDTO();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFollowerCount(user.getFollowers() != null ? user.getFollowers().size() : 0);
        dto.setFollowingCount(user.getFollowing() != null ? user.getFollowing().size() : 0);

        // Map the user's posts to clean DTOs
        if (user.getPosts() != null) {
            List<UserPostDTO> postDTOs = user.getPosts().stream().map(post -> {
                UserPostDTO pDto = new UserPostDTO();
                pDto.setPostId(post.getId());
                pDto.setTitle(post.getTitle());
                pDto.setContent(post.getContent());
                pDto.setCreatedAt(post.getCreatedAt());
                pDto.setLikeCount(post.getLikes() != null ? post.getLikes().size() : 0);
                pDto.setCommentCount(post.getComments() != null ? post.getComments().size() : 0);
                return pDto;
            }).collect(Collectors.toList());
            dto.setPosts(postDTOs);
        } else {
            dto.setPosts(new ArrayList<>());
        }
        return dto;
    }
}