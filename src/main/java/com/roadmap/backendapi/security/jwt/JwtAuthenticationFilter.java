package com.roadmap.backendapi.security.jwt;

import com.google.common.util.concurrent.RateLimiter;
import com.roadmap.backendapi.exception.user.AlreadyLoggedOutException;
import com.roadmap.backendapi.exception.user.AuthenticationException;
import com.roadmap.backendapi.exception.user.unsuccessfulAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Filter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JwtAuthenticationFilter is a Spring Security filter that processes JWT authentication.
 * It checks for the presence of a JWT token in the request, validates it, and sets the
 * authentication context if valid. It also handles rate limiting and security headers.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RateLimiter rateLimiter = RateLimiter.create(10); // 10 requests/second

    /**
     * Constructor for JwtAuthenticationFilter.
     *
     * @param jwtService         The JWT service for token operations.
     * @param userDetailsService The user details service for loading user details.
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters incoming requests to check for JWT authentication.
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain.
     * @throws ServletException if an error occurs during filtering.
     * @throws IOException      if an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Skip authentication for certain endpoints
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
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("MISSING JWT TOKEN");
                return;
            }

            // 3. Token validation
            String username = jwtService.getUsername(jwt);
            // Check if the token is present in the token store
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.validateToken(jwt, userDetails)) {
                    setAuthenticationInContext(request, userDetails);
                    if(jwtService.isWithinLogoutTime(jwt)){
                        response.setStatus(HttpStatus.BAD_REQUEST.value());
                        response.getWriter().write("User is already logged out");
                        return;
                    }
                    else if (jwtService.isTokenExpired(jwt)) {
                        jwtService.handleTokenRefresh(response, jwt);
                    }
                }
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                // add body to the response to contain the error message in a JSON format
                response.getWriter().write("INVALID JWT TOKEN");
                return;
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

    /**
     * Parses the JWT token from the Authorization header.
     *
     * @param request The HTTP request.
     * @return The JWT token or null if not found.
     */
    private String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        // Check if the header is present and starts with "Bearer "
        return (header != null && header.startsWith("Bearer "))
                // Extract the token from the header
                ? header.substring(7)
                // Return null if the header is not present or does not start with "Bearer
                : null;
    }

    /**
     * Sets the authentication in the security context.
     *
     * @param request      The HTTP request.
     * @param userDetails  The user details.
     */
    private void setAuthenticationInContext(HttpServletRequest request, UserDetails userDetails) {
        // Create an authentication token with the user details and set it in the security context
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        // Set the authentication details
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    /**
     * Sets security headers in the HTTP response.
     *
     * @param response The HTTP response.
     */
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
