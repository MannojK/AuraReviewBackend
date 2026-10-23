package org.project.social.socailmediadb.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LikeDTO {
    private String username; // Changed from Long userId!
        private Long postId;
    private String voteType; // "up", "down", or null
}