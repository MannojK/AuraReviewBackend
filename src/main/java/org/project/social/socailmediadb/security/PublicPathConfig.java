package org.project.social.socailmediadb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PublicPathConfig {
    @Bean
    public List<String> publicPaths(){
        return List.of("/api/v1/posts/**","/api/v1/auth/**"
                ,"/api/v1/comments/**"
                ,"/api/v1/categories/**","/api/v1/users/**","/api/v1/likes/**");
    }
}
