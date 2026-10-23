package org.project.social.socailmediadb.repository;

import org.project.social.socailmediadb.entity.Category;
import org.project.social.socailmediadb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//    Optional<Category> findByNameIgnoreCase(String trimmedName);

    Optional<Category> findByCategoryTypeIgnoreCase(String trimmedType);

//    List<Post> findByPostId(Long categoryId);

}