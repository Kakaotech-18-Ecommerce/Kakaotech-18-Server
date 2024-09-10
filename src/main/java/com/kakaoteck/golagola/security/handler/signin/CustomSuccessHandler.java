package com.kakaoteck.golagola.security.handler.signin;

import com.kakaoteck.golagola.domain.auth.Repository.UserRepository;
import com.kakaoteck.golagola.domain.auth.dto.CustomOAuth2User;
import com.kakaoteck.golagola.domain.auth.entity.UserEntity;
import com.kakaoteck.golagola.security.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@Component
public class
CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    // AutoWired로 대체 가능한가요?
    public CustomSuccessHandler(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // JWT 생성
        String token = jwtUtil.createJwt(username, role, 60*60*60L);

        // Refresh Token 생성 및 저장
        String refreshToken = jwtUtil.createJwt(username, role, 7*24*60*60L); // 예: 7일간 유효한 리프레시 토큰

        // UserEntity 업데이트
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            userEntity.setRefreshToken(refreshToken);
            userEntity.setLoginStatus(true); // 로그인 상태를 true로 설정
            userRepository.save(userEntity); // 업데이트된 정보를 저장
        }

        // 쿠키 설정
        response.addCookie(createCookie("Authorization", token)); // 쿠키를 넣어준다.
        response.addCookie(createCookie("RefreshToken", refreshToken)); // 리프레시 토큰도 쿠키로 추가
        response.sendRedirect("http://localhost:3000?login=success"); // 프론트쪽으로 특정 uri로 리다이렉트
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true); // js가 해당 쿠키를 가져가지 못하도록

        return cookie;
    }
}
