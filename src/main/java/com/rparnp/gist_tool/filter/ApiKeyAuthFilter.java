package com.rparnp.gist_tool.filter;

import com.rparnp.gist_tool.config.ToolConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(ApiKeyAuthFilter.class);

    private final String API_KEY_HEADER = "X-API-KEY";
    @Value("${gist.tool.apikey}")
    private String API_KEY;

    @Autowired
    private ToolConfig toolConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestApiKey = request.getHeader(API_KEY_HEADER);

        if (requestApiKey != null && requestApiKey.equals(API_KEY)) {
            filterChain.doFilter(request, response);
        } else {
            String logMessage = requestApiKey == null ? "Failed api key authentication with no header" :
                    "Failed api key authentication with api key: " + requestApiKey;
            logger.info(logMessage);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Unauthorized");
        }
    }
}
