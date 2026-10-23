package org.project.social.socailmediadb.controller;

import lombok.RequiredArgsConstructor;
import org.project.social.socailmediadb.dto.LikeDTO;
import org.project.social.socailmediadb.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // Endpoint to Like or Unlike a post
    // Endpoint to Like, Dislike, or Remove a vote on a post
    // Changed to /vote to exactly match your React API call!
    @PostMapping("/{postId}/vote")
    public ResponseEntity<String> voteOnPost(
            @PathVariable Long postId,
            @RequestBody LikeDTO likeDto) {

        // 1. Link the post ID from the URL to the DTO
        likeDto.setPostId(postId);

        // ⚠️ CRITICAL STEP: The User ID
        // React is only sending { voteType: "up" }. It is NOT sending a userId.
        // If you don't set a userId here, your service will crash trying to find User(null).
        // For testing right now, hardcode a user ID that exists in your database (e.g., 1L).
//        likeDto.setUserId(1L);

        // 2. Call the new handleVote method we created in the service
        String result = likeService.handleVote(likeDto);

        return ResponseEntity.ok(result);
    }

    // You can keep this GetMapping, though React shouldn't need to call it directly
    // since we added getLikeCount() directly to the Post entity!
    @GetMapping("/{postId}/likes/count")
    public ResponseEntity<Long> getLikesCount(@PathVariable Long postId) {
        long count = likeService.getLikesCount(postId);
        return ResponseEntity.ok(count);
    }
}