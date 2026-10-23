package org.project.social.socailmediadb.service;

import jakarta.transaction.Transactional;
import org.project.social.socailmediadb.dto.CategoryDTO;
import org.project.social.socailmediadb.dto.PostRequestDTO;
import org.project.social.socailmediadb.dto.PostResponseDTO;
import org.project.social.socailmediadb.dto.CommentDTO;
import org.project.social.socailmediadb.entity.Category;
import org.project.social.socailmediadb.entity.Comment;
import org.project.social.socailmediadb.entity.Post;
import org.project.social.socailmediadb.entity.User;
import org.project.social.socailmediadb.repository.CategoryRepository;
import org.project.social.socailmediadb.repository.CommentRepository;
import org.project.social.socailmediadb.repository.PostRepository;
import org.project.social.socailmediadb.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Transactional
    public PostResponseDTO createPost(PostRequestDTO request) {

        // 1. Fetch User (Creator of the post)
        // FIX: Use findByUsername instead of findById
        User user = (User) userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + request.getUsername()));

        // 2. Resolve Category (Find existing by name or create a new one)
        Category category = resolveCategory(request.getCategory());

        // 3. Map Request DTO -> Post Entity
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setUser(user);
        post.setCategory(category);

        // 4. Save to Database
        Post savedPost = postRepository.save(post);

        // 5. Map Entity -> PostResponseDTO
        return mapToResponseDTO(savedPost);
    }

    /**
     * Find existing category by name or create a new one on the fly.
     */
    private Category resolveCategory(String categoryTypeInput) {
        if (categoryTypeInput == null || categoryTypeInput.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }

        String trimmedType = categoryTypeInput.trim();

        return categoryRepository.findByCategoryTypeIgnoreCase(trimmedType)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setCategoryType(trimmedType); // ONLY keep this one!
                    return categoryRepository.save(newCategory);
                });
    }

    /**
     * Helper to map Entity to Response DTO
     */
    private PostResponseDTO mapToResponseDTO(Post post) {
        PostResponseDTO response = new PostResponseDTO();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setCategory(post.getCategory().getCategoryType());
        response.setUsername(post.getUser().getUsername());
        response.setCreatedAt(post.getCreatedAt());

        // 1. FIX COMMENTS AND COMMENT COUNT
        if (post.getComments() != null && !post.getComments().isEmpty()) {
            List<CommentDTO> commentDTOs = post.getComments().stream().map(c -> {
                CommentDTO cDto = new CommentDTO();
                cDto.setId(c.getId()); // Keeps the comment ID
                cDto.setText(c.getText());
                cDto.setCreatedAt(c.getCreatedAt());
                if (c.getUser() != null) {
                    // REMOVED: cDto.setId(c.getUser().getId()); <-- This was overwriting your Comment ID!
                    cDto.setUsername(c.getUser().getUsername());
                }
                return cDto;
            }).collect(Collectors.toList());

            response.setComments(commentDTOs);
            response.setCommentCount(commentDTOs.size()); // <-- FIX: Dynamically set comment count
        } else {
            response.setComments(new ArrayList<>());
            response.setCommentCount(0); // Fallback for 0 comments
        }

        // 2. FIX LIKE COUNT
        // If your Post entity holds a List of Like entities (e.g., post.getLikes()):
        if (post.getLikes() != null) {
            response.setLikeCount(post.getLikes().size());
        } else {
            response.setLikeCount(0);
        }

        /*
         * NOTE: If your Post entity just has an integer field for likes instead of a List,
         * delete the 'if' block above and use this instead:
         * response.setLikeCount(post.getLikeCount());
         */

        return response;
    }

    public List<PostResponseDTO> getAllPostsByCategory(Long categoryId ) {
        return postRepository.findByCategory_Id(categoryId)
                .stream()
                .map(this::transformToDTO)
                .collect(Collectors.toList());
    }

    private PostResponseDTO transformToDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        BeanUtils.copyProperties(post, dto);
//        dto.setId(post.getId());
        return dto;
    }

    // 2. READ ALL POSTS
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // 3. READ ONE POST
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return mapToResponseDTO(post);
    }

    // 4. UPDATE POST
    public PostResponseDTO updatePost(Long id, PostRequestDTO request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToResponseDTO(updatedPost);
    }

    // 5. DELETE POST
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(post);
    }

    // 6. ADD COMMENT TO POST
    @Transactional
    public CommentDTO addComment(Long postId, CommentDTO commentDTO) {
        // Find the target post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        // Create and set up comment entity
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        comment.setPost(post);

        // Optionally associate user if userId is provided in CommentDTO
        if (commentDTO.getId() != null) {
            User user = userRepository.findById(commentDTO.getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + commentDTO.getId()));
            comment.setUser(user);
        }

        // Save to database
        Comment savedComment = commentRepository.save(comment);

        // Map saved entity back to response DTO
        CommentDTO responseDTO = new CommentDTO();
        responseDTO.setId(savedComment.getId());
        responseDTO.setText(savedComment.getText());
        if (savedComment.getUser() != null) {
            responseDTO.setId(savedComment.getUser().getId());
            responseDTO.setUsername(savedComment.getUser().getUsername());
        }

        return responseDTO;
    }

//
//    // Helper method to convert Post Entity to PostResponseDTO safely
//    private PostResponseDTO mapToResponseDTO(Post post) {
//        PostResponseDTO dto = new PostResponseDTO();
//        dto.setId(post.getId());
//        dto.setTitle(post.getTitle());
//        dto.setContent(post.getContent());
//        dto.setCreatedAt(post.getCreatedAt());
//
//        if (post.getUser() != null) {
//            dto.setUsername(post.getUser().getUsername());
//        }
//
//        // Handle counts safely even if collections are null
//        dto.setLikeCount(post.getLikes() != null ? post.getLikes().size() : 0);
//        dto.setCommentCount(post.getComments() != null ? post.getComments().size() : 0);
//        dto.setCategoryType(post.getCategory() != null ? post.getCategory().getCategoryType() : "Uncategorized");
//
//        if (post.getComments() != null) {
//            List<CommentDTO> commentDTOs = post.getComments().stream().map(c -> {
//                CommentDTO cDto = new CommentDTO();
//                cDto.setId(c.getId());
//                cDto.setText(c.getText());
//                if (c.getUser() != null) {
//                    cDto.setId(c.getUser().getId());
//                    cDto.setUsername(c.getUser().getUsername());
//                }
//                return cDto;
//            }).collect(Collectors.toList());
//            dto.setComments(commentDTOs);
//        } else {
//            dto.setComments(new ArrayList<>());
//        }
//
//        return dto;
//    }
}