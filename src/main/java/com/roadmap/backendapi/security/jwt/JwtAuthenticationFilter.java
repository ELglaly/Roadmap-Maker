package com.roadmap.backendapi.security.jwt;

import com.google.common.util.concurrent.RateLimiter;
import com.roadmap.backendapi.exception.user.AlreadyLoggedOutException;
import com.roadmap.backendapi.exception.user.AuthenticationException;
import com.roadmap.backendapi.exception.user.unsuccessfulAuthentication;
import com.roadmap.backendapi.security.UserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Filter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RateLimiter rateLimiter = RateLimiter.create(10); // 10 requests/second

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    // private final AuditService auditService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        List<String> skipAuth = List.of("/api/v1/users/login", "/api/v1/users/register");
        if (skipAuth.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
                return;
        }

        try {
            // 1. Rate limiting
            if (!rateLimiter.tryAcquire()) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many requests");
                return;
            }

            // 2. JWT extraction
            final String jwt = parseJwt(request);
            if (jwt == null) {
                throw new AuthenticationException("Missing or invalid JWT token");
            }

            // 3. Token validation
            String username = jwtService.getUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername(username);

                if (jwtService.validateToken(jwt, userDetails)) {
                    setAuthenticationInContext(request, userDetails);
                    if(jwtService.isWithinLogoutTime(jwt)){
                        throw new AlreadyLoggedOutException("User is already logged out");
                    }
                    else if (jwtService.isTokenExpired(jwt)) {
                        jwtService.handleTokenRefresh(response, jwt);
                    }
                }
            } else {
                throw new AuthenticationException("Missing or invalid JWT token");
            }

            // 4. Security headers
            setSecurityHeaders(response);

        } catch (AuthenticationException ex) {
            // 5. Error handling
            response.setStatus(ex.getStatus().value());
            response.getWriter().write(ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return (header != null && header.startsWith("Bearer "))
                ? header.substring(7)
                : null;
    }

    private void setAuthenticationInContext(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    private void setSecurityHeaders(HttpServletResponse response) {
        // 1. Basic Security Headers
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "0"); // Disable legacy XSS filter
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        // 2. Modern Replacement for X-XSS-Protection
        response.setHeader("Content-Security-Policy",
                "default-src 'self'; " +
                        "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                        "style-src 'self' 'unsafe-inline'; " +
                        "img-src 'self' data:; " +
                        "connect-src 'self'; " +
                        "frame-ancestors 'none'; " +
                        "form-action 'self'; " +
                        "base-uri 'self'");

        // 3. Strict Transport Security (HSTS)
        response.setHeader("Strict-Transport-Security",
                "max-age=63072000; includeSubDomains; preload");

        // 4. Feature Policy (Deprecated but useful for older browsers)
        response.setHeader("Permissions-Policy",
                "geolocation=(), " +
                        "microphone=(), " +
                        "camera=(), " +
                        "payment=()");

        // 5. Cache Control for API responses
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }
    // Additional helper methods...
}
