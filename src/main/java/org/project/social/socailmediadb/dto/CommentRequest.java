package org.project.social.socailmediadb.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private String text;
    private Long postId;

    // Note: You usually don't send the userId from the frontend because
    // it can be spoofed. The backend should ideally extract the userId
    // from the logged-in user's JWT token or session.
    // But if you aren't using Spring Security yet, you can include it:
    // private Long userId;

    // Getters and Setters...
}