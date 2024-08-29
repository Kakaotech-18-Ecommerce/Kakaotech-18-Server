package com.kakaoteck.golagola.security.handler.signout;


import com.kakaoteck.golagola.domain.auth.Repository.UserRepository;
import com.kakaoteck.golagola.domain.auth.dto.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSignOutProcessHandler implements LogoutHandler {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication == null) {
            return;
        }

        // 1. 테리코드
//        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
//        userRepository.updateRefreshTokenAndLoginStatus(userPrincipal.getId(), null, false);

        // 2. 용우코드
        CustomOAuth2User userPrincipal = (CustomOAuth2User) authentication.getPrincipal(); // CustomOAuth2User 사용
        System.out.println("로그아웃 정보 확인"+ userPrincipal + userPrincipal.getUsername());
        userRepository.updateRefreshTokenAndLoginStatus(userPrincipal.getUsername(), null, false); // UserEntity에서 해당 유저를 찾아서 리프레시 토큰과 로그인 상태를 업데이트

    }


}







