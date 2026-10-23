package org.project.social.socailmediadb.security;

import lombok.RequiredArgsConstructor;
import org.project.social.socailmediadb.entity.User;
import org.project.social.socailmediadb.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AuraReviewUsernamePwdAuthenticationProvider  implements AuthenticationProvider {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        System.out.println("============== SECURITY DEBUG START ==============");
        System.out.println("1. Incoming Login Username: [" + username + "]");
        System.out.println("2. Incoming Login Raw Password: [" + pwd + "]");

        User user = userRepository.findByEmail(username).orElseThrow(
                () -> {
                    System.out.println("❌ ERROR: Username [" + username + "] not found in Database!");
                    return new UsernameNotFoundException("User details not found for the user: " + username);
                }
        );

        System.out.println("3. Found User in DB: " + user.getEmail());
        System.out.println("4. Stored Hash in DB: [" + user.getPasswordHash() + "]");

        boolean isMatch = passwordEncoder.matches(pwd, user.getPasswordHash());
        System.out.println("5. Does encoder match raw password to hash? -> " + isMatch);
        System.out.println("============== SECURITY DEBUG END ==============");

        if(isMatch) {
            return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        } else {
            throw new BadCredentialsException("Invalid password!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}