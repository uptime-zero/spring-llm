package com.springllm.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcLoggingFilter extends OncePerRequestFilter {

    private static final String HEADER_REQUEST_ID = "X-Request-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            String requestId = Optional.ofNullable(request.getHeader(HEADER_REQUEST_ID))
                    .filter(h -> !h.isBlank())
                    .orElse(UUID.randomUUID().toString());

            MDC.put("requestId", requestId);
            MDC.put("httpMethod", request.getMethod());
            MDC.put("requestUri", request.getRequestURI());
            response.setHeader(HEADER_REQUEST_ID, requestId);

            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
