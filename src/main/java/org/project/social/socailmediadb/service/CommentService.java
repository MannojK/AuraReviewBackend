package org.project.social.socailmediadb.service;

import jakarta.transaction.Transactional;
import org.project.social.socailmediadb.dto.CommentDTO;
import org.project.social.socailmediadb.entity.Comment;
import org.project.social.socailmediadb.entity.Post;
import org.project.social.socailmediadb.entity.User;
import org.project.social.socailmediadb.repository.CommentRepository;
import org.project.social.socailmediadb.repository.PostRepository;
import org.project.social.socailmediadb.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private final UserRepository userRepository;

    public CommentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. GET COMMENTS
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(this::transformToDTO)
                .collect(Collectors.toList());
    }

    // GET COMMENTS
    public List<CommentDTO> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserId(userId)
                .stream()
                .map(this::transformToDTO)
                .collect(Collectors.toList());
    }

    // 2. ADD COMMENT
    public CommentDTO addComment(Long postId, CommentDTO dto) {
        // 1. Fetch dependencies
        Post post = postRepository.findById(postId).orElseThrow();
        User user = (User) userRepository.findByUsername(dto.getUsername()).orElseThrow();

        // 2. Map manually
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setPost(post);
        comment.setUser(user);

        // --> THE FIX: Stamp the exact time right here! <--
        comment.setCreatedAt(LocalDateTime.now());
        // 3. Save to database
        Comment savedComment = commentRepository.save(comment);

        // 4. Return the fully populated DTO
        return transformToDTO(savedComment);
    }

    private CommentDTO transformToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setUsername(comment.getUser().getUsername());
        dto.setCreatedAt(comment.getCreatedAt()); // Grabs the accurate DB time
        return dto;
    }

    @Transactional
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        // 1. Fetch the Post from the DB
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        // 2. Fetch the User from the DB
        // Assuming your frontend sends the username inside the CommentDTO
        User user = (User) userRepository.findByUsername(commentDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + commentDTO.getUsername()));

        // 3. Map DTO to Entity
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        comment.setPost(post);
        comment.setUser(user);
        // Note: We don't set 'id' or 'createdAt' here because the database
        // handles the ID generation and timestamping.

        // 4. Save the entity to the database
        Comment savedComment = commentRepository.save(comment);

        // 5. Map the saved Entity back to your CommentDTO to return to the frontend
        CommentDTO responseDTO = new CommentDTO();
        responseDTO.setId(savedComment.getId());
        responseDTO.setText(savedComment.getText());
        responseDTO.setUsername(savedComment.getUser().getUsername());
        responseDTO.setCreatedAt(savedComment.getCreatedAt());

        return responseDTO;
    }

    // Helper method keeping the main methods short and readable
//    private CommentDTO transformToDTO(Comment comment) {
//        CommentDTO dto = new CommentDTO();
//        BeanUtils.copyProperties(comment, dto);
//        return dto;
//    }



    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}