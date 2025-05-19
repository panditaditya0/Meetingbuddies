package com.meetingBuddy.meetingBuddy.Filters;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class ExternalCheckFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            ResponseEntity<String> res = restTemplate.getForEntity("http://139.99.9.42:8585/test", String.class);
            if (!res.getStatusCode().is2xxSuccessful()) {
                response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "External service check failed");
                return;
            }
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "External service unreachable");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
