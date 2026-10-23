package org.project.social.socailmediadb.service;

import jakarta.transaction.Transactional;
import org.project.social.socailmediadb.dto.CategoryDTO;
import org.project.social.socailmediadb.dto.CommentDTO;
import org.project.social.socailmediadb.dto.PostRequestDTO;
import org.project.social.socailmediadb.dto.PostResponseDTO;
import org.project.social.socailmediadb.entity.Category;
import org.project.social.socailmediadb.entity.Comment;
import org.project.social.socailmediadb.entity.Post;
import org.project.social.socailmediadb.repository.CategoryRepository;
import org.project.social.socailmediadb.repository.CommentRepository;
import org.project.social.socailmediadb.repository.PostRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    public List<CategoryDTO> findAllCategories() {
       return categoryRepository.findAll()
               .stream()
               .map(this::transformToDTO)
               .collect(Collectors.toList());
    }

    @Transactional // THIS IS CRITICAL
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return transformToDTO(category);
    }

    public CategoryDTO transformToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, categoryDTO);

        if (category.getPosts() != null) {
            // Pass the categoryType down to the post mapper directly
            List<PostResponseDTO> postDTOs = category.getPosts().stream()
                    .map(post -> transformPostToDTO(post, category.getCategoryType()))
                    .collect(Collectors.toList());
            categoryDTO.setPosts(postDTOs);
        }

        return categoryDTO;
    }

    // Notice we added a second parameter for the categoryType string
    public PostResponseDTO transformPostToDTO(Post post, String categoryType) {
        PostResponseDTO postDTO = new PostResponseDTO();
        BeanUtils.copyProperties(post, postDTO);

        // 1. Manually calculate likeCount from Set<Like>
        if (post.getLikes() != null) {
            postDTO.setLikeCount(post.getLikes().size());
        } else {
            postDTO.setLikeCount(0);
        }

        // 2. Set Category using the string we passed in
        postDTO.setCategory(categoryType);

        // 3. Manually map the User to username
        if (post.getUser() != null) {
            postDTO.setUsername(post.getUser().getUsername());
        }

        // 4. Manually map Comments
        if (post.getComments() != null && !post.getComments().isEmpty()) {
            List<CommentDTO> commentDTOs = post.getComments().stream()
                    .map(this::transformCommentToDTO)
                    .collect(Collectors.toList());
            postDTO.setComments(commentDTOs);
            postDTO.setCommentCount(commentDTOs.size());
        } else {
            postDTO.setComments(new ArrayList<>());
            postDTO.setCommentCount(0);
        }

        return postDTO;
    }

    public CommentDTO transformCommentToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(comment, commentDTO);

        if (comment.getUser() != null) {
            commentDTO.setUsername(comment.getUser().getUsername());
        }
        return commentDTO;
    }
    public CategoryDTO saveCategory(CategoryDTO dto) {
        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        Category savedCategory = categoryRepository.save(category);
        return transformToDTO(savedCategory);
    }




    public void deleteCategory(long id){
        categoryRepository.deleteById(id);
    }
}
