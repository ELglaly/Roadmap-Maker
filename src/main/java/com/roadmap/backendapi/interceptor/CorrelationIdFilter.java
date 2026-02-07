package com.roadmap.backendapi.interceptor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter to add correlation IDs to requests for distributed tracing.
 * The correlation ID is:
 * 1. Extracted from X-Correlation-ID header if present
 * 2. Otherwise, a new UUID is generated
 * 3. Added to MDC (Mapped Diagnostic Context) for logging
 * 4. Added to response headers for client tracking
 */
@Component
@Order(1) // Execute early in the filter chain
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CORRELATION_ID_MDC_KEY = "correlationId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // Extract or generate correlation ID
            String correlationId = request.getHeader(CORRELATION_ID_HEADER);
            if (correlationId == null || correlationId.trim().isEmpty()) {
                correlationId = UUID.randomUUID().toString();
            }

            // Add to MDC for logging
            MDC.put(CORRELATION_ID_MDC_KEY, correlationId);

            // Add to response headers
            response.setHeader(CORRELATION_ID_HEADER, correlationId);

            // Continue with request processing
            filterChain.doFilter(request, response);

        } finally {
            // Clean up MDC to prevent memory leaks
            MDC.clear();
        }
    }
}
