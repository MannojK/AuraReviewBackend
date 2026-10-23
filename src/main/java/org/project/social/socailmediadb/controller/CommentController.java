package org.project.social.socailmediadb.controller;


import org.project.social.socailmediadb.dto.CommentDTO;
import org.project.social.socailmediadb.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // GET /api/posts/1/comments
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    // GET /api/comments/post/123
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByUserId(@PathVariable Long userId) {
        List<CommentDTO> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

    // POST /api/posts/1/comments

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO) {

        // ADD THIS LINE: Let's see what Java actually received!
        System.out.println("RECEIVED COMMENT: text=" + commentDTO.getText() + ", username=" + commentDTO.getUsername());

        CommentDTO savedComment = commentService.addComment(postId, commentDTO);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }


}