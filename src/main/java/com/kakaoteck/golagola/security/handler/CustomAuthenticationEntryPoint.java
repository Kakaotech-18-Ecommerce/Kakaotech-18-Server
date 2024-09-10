package com.kakaoteck.golagola.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaoteck.golagola.global.common.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // 402 상태 코드와 커스텀 에러 메시지 반환
        response.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // ApiResponse 객체 생성
        ApiResponse<Object> apiResponse = ApiResponse.onFailure("402",
                "Access denied: Please provide valid credentials.");

        // JSON 변환 및 응답 작성
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
    }
}
