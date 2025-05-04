// JwtAuthenticationFilter.java
package com.roadmap.backendapi.security.jwt;

import com.google.common.util.concurrent.RateLimiter;
import com.roadmap.backendapi.exception.user.AuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RateLimiter rateLimiter = RateLimiter.create(10);
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/login",
            "/register"
    );

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Set security headers early for all responses
        setSecurityHeaders(response);

        if (PUBLIC_ENDPOINTS.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Apply rate limiting only to non-public endpoints
            if (!rateLimiter.tryAcquire()) {
                sendError(response, HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
                return;
            }

            final String jwt = parseJwt(request);
            if (jwt == null) {
                sendError(response, HttpStatus.UNAUTHORIZED, "Missing JWT token");
                return;
            }

            String username = jwtService.getUsername(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtService.validateToken(jwt, userDetails)) {
                sendError(response, HttpStatus.UNAUTHORIZED, "Invalid token");
                return;
            }

            setAuthenticationInContext(request, userDetails);
            filterChain.doFilter(request, response);

        } catch (AuthenticationException | UsernameNotFoundException e) {
            sendError(response, HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            sendError(response, HttpStatus.INTERNAL_SERVER_ERROR, "Authentication error");
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null) {
            header = header.trim();
            if (header.startsWith("Bearer ") || header.startsWith("bearer ")) {
                return header.substring(7);
            }
        }
        return null;
    }

    private void setAuthenticationInContext(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void setSecurityHeaders(HttpServletResponse response) {
        response.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self'");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Referrer-Policy", "no-referrer");
        response.setHeader("Permissions-Policy", "geolocation=(self), microphone=()"); // Adjust as needed
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

    }

    private void sendError(HttpServletResponse response, HttpStatus status, String message)
            throws IOException {
        response.setStatus(status.value());
        response.getWriter().write(message);
        response.getWriter().flush();
    }
}