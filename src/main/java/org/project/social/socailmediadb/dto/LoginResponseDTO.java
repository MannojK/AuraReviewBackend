package org.project.social.socailmediadb.dto;


public record LoginResponseDTO(String message, UserRequestDTO user , String jwtToken) {
}
