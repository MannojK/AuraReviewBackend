package org.project.social.socailmediadb.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String categoryType;
    private List<PostResponseDTO> posts; // Use Response DTO here
}
