package org.project.social.socailmediadb.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter


public class PostRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 50, message = "Title must be between 5 and 50 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 5, max = 10000, message = "Content must be between 5 and 10000 characters")
    private String content;

    @NotBlank(message = "Category is required")
    @Size(min = 5, max = 20, message = "Category must be between 5 and 20 characters")
    private String category;

    @NotBlank(message = "Username is missing. Please log in again.")
    private String username;

    // Getters and Setters below...
}