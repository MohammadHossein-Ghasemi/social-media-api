package com.muhu.SocialMediaApi.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        LocalDateTime currentTimeStamp = LocalDateTime.now();

        String message = (authException != null && authException.getMessage() != null) ?
                authException.getMessage() : "Unauthorized";

        String path = request.getRequestURI();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.setContentType("application/json;charset=UTF-8");

        String jsonResponse =
                String.format("{\"timestamp\": \"%s\", \"status\": %d, \"message\": \"%s\", \"path\": \"%s\"}",
                        currentTimeStamp, HttpStatus.UNAUTHORIZED.value(),message, path);

        response.getWriter().write(jsonResponse);
    }
}
