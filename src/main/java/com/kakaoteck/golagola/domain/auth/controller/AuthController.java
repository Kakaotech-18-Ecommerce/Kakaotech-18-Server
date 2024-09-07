package com.kakaoteck.golagola.domain.auth.controller;


import com.kakaoteck.golagola.domain.auth.dto.AuthRequest;
import com.kakaoteck.golagola.domain.auth.dto.CustomOAuth2User;
import com.kakaoteck.golagola.domain.auth.service.AuthService1;
import com.kakaoteck.golagola.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService1 authService;

    @Operation(summary = "회원가입 추가정보 진행", description = "(nickname, gender) 저장")
    @PostMapping("/join")
    public ApiResponse<String> join(@RequestBody AuthRequest authRequest) {
        // 1. jwt 세션 접근
        CustomOAuth2User customUser = (CustomOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = customUser.getUsername();

        // 2. UserService를 통해 (nickname, gender) 저장
        authService.saveUserDetails(username, authRequest);

        return ApiResponse.onSuccess("회원가입 성공");
    }
}
