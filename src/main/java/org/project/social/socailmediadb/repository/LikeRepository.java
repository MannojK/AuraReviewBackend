package org.project.social.socailmediadb.repository;

import org.project.social.socailmediadb.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUser_UsernameAndPost_Id(String username, Long postId);

    // Add this new method!
    long countByPostIdAndVoteType(Long postId, String voteType);
}