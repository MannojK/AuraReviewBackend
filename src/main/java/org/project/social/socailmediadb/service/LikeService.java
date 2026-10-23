package org.project.social.socailmediadb.service;

import lombok.RequiredArgsConstructor;
import org.project.social.socailmediadb.dto.LikeDTO;
import org.project.social.socailmediadb.entity.Like;
import org.project.social.socailmediadb.entity.Post;
import org.project.social.socailmediadb.entity.User;
import org.project.social.socailmediadb.repository.LikeRepository;
import org.project.social.socailmediadb.repository.PostRepository;
import org.project.social.socailmediadb.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    @Transactional
    public String handleVote(LikeDTO likeDto) {
        // 1. Debug log to guarantee we are receiving the data
        System.out.println("DEBUG: User " + likeDto.getUsername() + " voting " + likeDto.getVoteType() + " on Post " + likeDto.getPostId());

        User user = (User) userRepository.findByUsername(likeDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(likeDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<Like> existingVote = likeRepository.findByUser_UsernameAndPost_Id(
                likeDto.getUsername(),
                likeDto.getPostId()
        );

        // 2. FORCE the SQL queries to execute IMMEDIATELY using saveAndFlush()
        if (likeDto.getVoteType() == null || likeDto.getVoteType().trim().isEmpty()) {
            if (existingVote.isPresent()) {
                Like voteToRemove = existingVote.get();
                post.getLikes().remove(voteToRemove); // Sync the relationship
                likeRepository.delete(voteToRemove);
                likeRepository.flush(); // Force SQL DELETE
            }
        } else if (existingVote.isPresent()) {
            Like vote = existingVote.get();
            vote.setVoteType(likeDto.getVoteType());
            likeRepository.saveAndFlush(vote); // Force SQL UPDATE
        } else {
            Like newVote = new Like();
            newVote.setUser(user);
            newVote.setPost(post);
            newVote.setVoteType(likeDto.getVoteType());

            post.getLikes().add(newVote); // Sync the relationship
            likeRepository.saveAndFlush(newVote); // Force SQL INSERT
        }

        // 3. Count exactly what is in the DB right now
        long upvotes = likeRepository.countByPostIdAndVoteType(post.getId(), "up");
        long downvotes = likeRepository.countByPostIdAndVoteType(post.getId(), "down");

        // 4. Update the post score and force the DB to save it
        post.setLikeCount((int) (upvotes - downvotes));
        postRepository.saveAndFlush(post);

        System.out.println("DEBUG: Post " + post.getId() + " new score is " + post.getLikeCount());

        return "Vote processed successfully";
    }
//    @Transactional
//    public String toggleLike(LikeDTO likeDto) {
//        // 1. Fetch User and Post (Throw exceptions if they don't exist)
//        User user = (User) userRepository.findByUsername(likeDto.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Post post = postRepository.findById(likeDto.getPostId())
//                .orElseThrow(() -> new RuntimeException("Post not found"));
//
//
//        // 2. Check if the like already exists
//        return likeRepository.findByUserIdAndPostId(user.getId(), post.getId())
//                .map(existingLike -> {
//                    // If it exists, remove it (Unlike)
//                    likeRepository.delete(existingLike);
//                    return "Post unliked successfully";
//                })
//                .orElseGet(() -> {
//                    // If it doesn't exist, create it (Like)
//                    Like newLike = new Like();
//                    newLike.setUser(user);
//                    newLike.setPost(post);
//                    likeRepository.save(newLike);
//                    return "Post liked successfully";
//                });
//    }
@Transactional(readOnly = true)
public long getLikesCount(Long postId) {
    // 1. Get the total number of upvotes
    long upvotes = likeRepository.countByPostIdAndVoteType(postId, "up");

    // 2. Get the total number of downvotes
    long downvotes = likeRepository.countByPostIdAndVoteType(postId, "down");

    // 3. Return the Reddit-style score
    return upvotes - downvotes;
}
}