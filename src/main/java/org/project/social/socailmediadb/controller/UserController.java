package org.project.social.socailmediadb.controller;

import org.project.social.socailmediadb.dto.CommentDTO;
import org.project.social.socailmediadb.dto.UserRequestDTO;
import org.project.social.socailmediadb.dto.UserResponseDTO;
import org.project.social.socailmediadb.dto.UserWithPostsResponseDTO;
import org.project.social.socailmediadb.service.CommentService;
import org.project.social.socailmediadb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    // HTTP POST - Register a new user
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO request) {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    // HTTP GET - Get all users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // HTTP GET - Get a single profile
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // HTTP PUT - Update user account information
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // HTTP DELETE - Remove a user profile
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // HTTP POST - Execute a network follow connection
    // URL Example: POST /api/users/1/follow/2 (User 1 follows User 2)
    @PostMapping("/{followerId}/follow/{followingId}")
    public ResponseEntity<String> followUser(@PathVariable Long followerId, @PathVariable Long followingId) {
        userService.followUser(followerId, followingId);
        return ResponseEntity.ok("Successfully followed the user.");
    }

    // HTTP DELETE - Terminate a network follow connection
    @DeleteMapping("/{followerId}/unfollow/{followingId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Long followerId, @PathVariable Long followingId) {
        userService.unfollowUser(followerId, followingId);
        return ResponseEntity.ok("Successfully unfollowed the user.");
    }

    // HTTP GET - Get a user profile along with all their posts
    @GetMapping("/{id}/posts")
    public ResponseEntity<UserWithPostsResponseDTO> getUserWithPosts(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserWithPosts(id));
    }

}