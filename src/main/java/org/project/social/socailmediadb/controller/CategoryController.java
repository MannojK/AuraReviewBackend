package org.project.social.socailmediadb.controller;


import org.project.social.socailmediadb.dto.CategoryDTO;
import org.project.social.socailmediadb.dto.PostResponseDTO;
import org.project.social.socailmediadb.service.CategoryService;
import org.project.social.socailmediadb.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PostService postService;
   @GetMapping
    public List<CategoryDTO> findAll(){
        return categoryService.findAllCategories();
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.saveCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryDTO);
    }
    @GetMapping("{id}/posts")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){
        return  ResponseEntity.ok(categoryService.getCategoryById(id));

    }
}
