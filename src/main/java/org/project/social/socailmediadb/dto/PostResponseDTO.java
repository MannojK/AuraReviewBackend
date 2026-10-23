package org.project.social.socailmediadb.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String category; // Flat structural target field for UI layout
    private String username;
    private int likeCount;
    private int commentCount;
    private List<CommentDTO> comments;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String userVoteStatus;


    // Getters, Setters
}
