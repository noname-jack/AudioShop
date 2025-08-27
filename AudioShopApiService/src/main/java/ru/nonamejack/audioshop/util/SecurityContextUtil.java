package ru.nonamejack.audioshop.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {
    public static Integer getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            try {
                return Integer.parseInt(jwt.getSubject());
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid user ID in JWT subject: " + jwt.getSubject(), e);
            }
        }
        throw new SecurityException("User not authenticated");
    }
}
