package org.project.social.socailmediadb.repository;

import org.project.social.socailmediadb.dto.PostResponseDTO;
import org.project.social.socailmediadb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

//    Add an underscore _ to explicitly signal to Spring
//    Data JPA where to split the property traversal (from the category
//  object to its id property):
    List<Post> findByCategory_Id(Long categoryId);
}



//    @Query("SELECT p FROM Post p WHERE p.category.id = :categoryId")
//    List<Post> findByCategoryId(@Param("categoryId") Long categoryId);

