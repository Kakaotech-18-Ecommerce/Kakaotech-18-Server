package com.kakaoteck.golagola.security.jwt;

import com.kakaoteck.golagola.domain.auth.Repository.UserRepository;
import com.kakaoteck.golagola.domain.auth.dto.CustomOAuth2User;
import com.kakaoteck.golagola.domain.auth.dto.UserDTO;
import com.kakaoteck.golagola.domain.auth.entity.UserEntity;
import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.global.common.enums.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository; // UserRepository 주입

    public JWTFilter(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository; // UserRepository 초기화
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // jwt 기간 만료시, 무한 재로그인 방지 로직
        String requestUri = request.getRequestURI();
        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }

        // cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String authorization = null;
        Cookie[] cookies = request.getCookies();

        // 쿠키가 null인지 확인
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName());
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
                }
            }
        }
        // Authorization 헤더 검증
        if (authorization == null) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return; // 조건이 해당되면 메소드 종료 (필수)
        }

        // 토큰
        String token = authorization;

        // 토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            return; // 조건이 해당되면 메소드 종료 (필수)
        }

        // 토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);

        // 1. username으로 데이터베이스에서 사용자 정보 조회
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        if (userEntityOptional.isEmpty()) {
            filterChain.doFilter(request, response);
            return; // 사용자 정보가 없으면 필터 종료
        }

        UserEntity userEntity = userEntityOptional.get();

        String role = jwtUtil.getRole(token);
        System.out.println("jwtfilter jwt확인: " + username + role);

        // userDTO를 생성하여 값 set
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setName(userEntity.getName());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setNickname(userEntity.getNickname()); // 추가 정보 설정
        userDTO.setGender(userEntity.getGender()); // 추가 정보 설정
//        userDTO.setRole(role); // buyer, seller받아오는걸로 바꾸기

        Authentication authToken = null;
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO, userEntity); // UserDetails에 회원 정보 객체 담기
        // 연관된 1대1 매핑에 값이 존재할 때(CustomOAuth2User, buyer, seller)로 받기
        if (userEntity.getRole() == Role.BUYER) {
            Buyer buyer = userEntity.getBuyer();  // 1:1 매핑된 Buyer 가져오기
            authToken = new UsernamePasswordAuthenticationToken(buyer, null, customOAuth2User.getAuthoritiesForRole(Role.BUYER));  // Buyer에 맞는 권한 설정
        }
        else if (userEntity.getRole() == Role.SELLER){
            Seller seller = userEntity.getSeller();  // 1:1 매핑된 Seller 가져오기
            authToken = new UsernamePasswordAuthenticationToken(seller, null, customOAuth2User.getAuthoritiesForRole(Role.SELLER));  // Seller에 맞는 권한 설정
        }
        else{
            authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities()); // 스프링 시큐리티 인증 토큰 생성, 스프링 시큐리티에서 세션을 생성해가지고 토큰을 등록하고 있음.
        }
        SecurityContextHolder.getContext().setAuthentication(authToken); // 세션에 사용자 등록
        filterChain.doFilter(request, response); // jwtfilter작업을 다 했기 때문에 다음 필터에게 작업을 넘긴다는 doFilter작업을 진행해주시면 됩니다.
    }

}
