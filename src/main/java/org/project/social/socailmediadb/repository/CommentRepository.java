package org.project.social.socailmediadb.repository;

import org.project.social.socailmediadb.dto.CommentDTO;
import org.project.social.socailmediadb.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // JOIN FETCH grabs the User entity in the same single SQL query
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.user WHERE c.post.id = :postId")
    List<Comment> findByPostId(@Param("postId") Long postId);

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.user WHERE c.user.id = :userId")
    List<Comment> findByUserId(@Param("userId") Long userId);
}