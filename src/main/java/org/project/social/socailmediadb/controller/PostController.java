package org.project.social.socailmediadb.controller;

import jakarta.validation.Valid;
import org.project.social.socailmediadb.dto.CommentDTO;
import org.project.social.socailmediadb.dto.PostRequestDTO;
import org.project.social.socailmediadb.dto.PostResponseDTO;
import org.project.social.socailmediadb.entity.Like;
import org.project.social.socailmediadb.entity.Post;
import org.project.social.socailmediadb.repository.LikeRepository;
import org.project.social.socailmediadb.repository.PostRepository;
import org.project.social.socailmediadb.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostRepository postRepository;


    // HTTP POST - Create a new post
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@Valid  @RequestBody PostRequestDTO request) {
        return new ResponseEntity<>(postService.createPost(request), HttpStatus.CREATED);
    }

    // HTTP GET - Get all posts
//    @GetMapping
//    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
//        return ResponseEntity.ok(postService.getAllPosts());
//    }
//    @GetMapping("/{id}")
//    public ResponseEntity<Post> getPost(
//            @PathVariable Long id,
//            @RequestParam(required = false) String username) { // Accept username from React
//
//        Post post = postRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Post not found"));
//
//        // If React sent a username, figure out if they voted on this post!
//        if (username != null && !username.isEmpty()) {
//            Optional<Like> vote = likeRepository.findByUser_UsernameAndPost_Id(username, id);
//            if (vote.isPresent()) {
//                post.setUserVoteStatus(vote.get().getVoteType());
//            }
//        }
//
//        return ResponseEntity.ok(post);
//    }
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts(
            @RequestParam(required = false) String username) { // 1. Accept username from React!

        // 2. Get the list of posts from your service
        List<PostResponseDTO> posts = postService.getAllPosts();

        // 3. If a user is logged in, figure out what they voted on for EVERY post
        if (username != null && !username.isEmpty()) {
            for (PostResponseDTO postDto : posts) {
                Optional<Like> vote = likeRepository.findByUser_UsernameAndPost_Id(username, postDto.getId());
                if (vote.isPresent()) {
                    postDto.setUserVoteStatus(vote.get().getVoteType());
                }
            }
        }

        return ResponseEntity.ok(posts);
    }
    // HTTP PUT - Update a post
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id, @RequestBody PostRequestDTO request) {
        return ResponseEntity.ok(postService.updatePost(id, request));
    }

    // HTTP DELETE - Delete a post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // Add this inside your PostController class
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addCommentToPost(
            @PathVariable Long id,
            @RequestBody CommentDTO commentDTO) {

        // Pass the post ID and comment details down to your service layer
        CommentDTO savedComment = postService.addComment(id, commentDTO);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }
}