package org.project.social.socailmediadb.dto;

import lombok.*;

import java.time.LocalDateTime;

// CommentDTO.java
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class CommentDTO {
    private Long id;
    private String text;
    private String username;

    private LocalDateTime createdAt;
    // Constructors, Getters, and Setters
}

// PostRequestDTO.java (Used for Creating/Updating)
