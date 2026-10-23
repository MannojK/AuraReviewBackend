package org.project.social.socailmediadb.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import  org.project.social.socailmediadb.constants.ApplicationConstants;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> publicPaths;

@Override
protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

    // Allow browser CORS preflight requests before JWT validation
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
        filterChain.doFilter(request, response);
        return;
    }

    String authHeader = request.getHeader(ApplicationConstants.JWT_HEADER);

    if (authHeader != null) {
        try {
            if (!authHeader.startsWith("Bearer ")) {
                throw new BadCredentialsException("Invalid Authorization header");
            }

            String jwt = authHeader.substring(7);
            Environment env = getEnvironment();

            if (env != null) {
                String secret = env.getProperty(
                        ApplicationConstants.JWT_SECRET_KEY,
                        ApplicationConstants.JWT_SECRET_DEFAULT_VALUE
                );

                SecretKey secretKey = Keys.hmacShaKeyFor(
                        secret.getBytes(StandardCharsets.UTF_8)
                );

                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();

                String username = String.valueOf(claims.get("email"));

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                username, null, Collections.emptyList()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        } catch (Exception exception) {
            throw new BadCredentialsException("Invalid Token received!");
        }
    }

    filterChain.doFilter(request, response);
}
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        return publicPaths.stream().anyMatch(publicPath ->
                pathMatcher.match(publicPath, path));
    }
}