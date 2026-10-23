package org.project.social.socailmediadb.controller;


import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.project.social.socailmediadb.Util.JwtUtil;
import org.project.social.socailmediadb.dto.LoginRequestDTO;
import org.project.social.socailmediadb.dto.LoginResponseDTO;
import org.project.social.socailmediadb.dto.RegisterRequestDTO;
import org.project.social.socailmediadb.dto.UserRequestDTO;
import org.project.social.socailmediadb.entity.User;
import org.project.social.socailmediadb.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Setter
@Getter
public class AuthController {


    //    private final User user;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompromisedPasswordChecker compromisedPasswordChecker;


//    @PostMapping("/login")
//    public ResponseEntity<LoginResponseDTO> apiLogin(@RequestBody
//                                                     LoginRequestDTO loginRequestDto) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(new
//                    UsernamePasswordAuthenticationToken(loginRequestDto.username(),
//                    loginRequestDto.password()));
//
//            var userDto = new UserRequestDTO();
//            var loggedInUser = (User) authentication.getPrincipal();
//            BeanUtils.copyProperties(loggedInUser, userDto);
//            String jwtToken = jwtUtil.generateJwtToken(authentication);
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(),
//                            userDto, jwtToken));
//        } catch (BadCredentialsException ex) {
//            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
//                    "Invalid username or password");
//        } catch (AuthenticationException ex) {
//            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
//                    "Authentication failed");
//        } catch (Exception ex) {
//            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
//                    "An unexpected error occurred");
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> apiLogin(@RequestBody LoginRequestDTO loginRequestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(loginRequestDto.username(),
                    loginRequestDto.password()));

            var userDto = new UserRequestDTO();
            var loggedInUser = (User) authentication.getPrincipal();
            BeanUtils.copyProperties(loggedInUser, userDto);
            String jwtToken = jwtUtil.generateJwtToken(authentication);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(),
                            userDto, jwtToken));
        } catch (BadCredentialsException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        } catch (AuthenticationException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Authentication failed");
        } catch (Exception ex) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDto) {

        CompromisedPasswordDecision decision = compromisedPasswordChecker.check(registerRequestDto.getPassword());
        if (decision.isCompromised()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("password", "Choose a strong password"));
        }

        Optional<User> existingCustomer = userRepository.findByEmailOrUsername(
                registerRequestDto.getEmail(),  registerRequestDto.getName());

        if (existingCustomer.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            User user = existingCustomer.get();

            if (user.getEmail().equalsIgnoreCase(registerRequestDto.getEmail())) {
                errors.put("email", "Email is already registered");
            }
            if (user.getUsername().equals(registerRequestDto.getName())) {
                errors.put("Username", " Username is already registered");
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        // --- FIX APPLIED HERE ---
        User user = new User();
        user.setUsername(registerRequestDto.getName());
        user.setEmail(registerRequestDto.getEmail());
        // Explicitly encode the password and set it ONLY to your hash field
        String hashedPassword = passwordEncoder.encode(registerRequestDto.getPassword());
        user.setPasswordHash(hashedPassword);
        // Ensure the plain text password field on the entity is explicitly null or empty
        // customer.setPassword(null);

        userRepository.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Registration successful");
    }

    private ResponseEntity<LoginResponseDTO> buildErrorResponse(HttpStatus status,
                                                                String message) {
        return ResponseEntity
                .status(status)
                .body(new LoginResponseDTO(message, null, null));
    }


}

