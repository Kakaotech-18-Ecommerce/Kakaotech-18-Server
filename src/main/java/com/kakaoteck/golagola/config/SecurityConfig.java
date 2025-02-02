package com.kakaoteck.golagola.config;

import com.kakaoteck.golagola.domain.auth.Repository.UserRepository;
import com.kakaoteck.golagola.security.handler.CustomAuthenticationEntryPoint;
import com.kakaoteck.golagola.security.handler.signout.CustomSignOutProcessHandler;
import com.kakaoteck.golagola.security.jwt.JWTFilter;
import com.kakaoteck.golagola.security.jwt.JWTUtil;
import com.kakaoteck.golagola.security.handler.signin.CustomSuccessHandler;
import com.kakaoteck.golagola.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final CustomSignOutProcessHandler customSignOutProcessHandler;
    private final UserRepository userRepository; // UserRepository 추가
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setMaxAge(3600L);
                configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                return configuration;
            }
        }));

        // CSRF 보호 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // 폼 로그인 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);

        // HTTP Basic 인증 비활성화
        http.httpBasic(AbstractHttpConfigurer::disable);

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                    //.failureHandler(oAuth2LoginFailureHandler) // 실패핸들러 추가하기
        );

        // 로그아웃 설정
        http.logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                //.addLogoutHandler(logoutHandler) // 지미꺼
                .addLogoutHandler(customSignOutProcessHandler) // 코이꺼
                .deleteCookies("JSESSIONID", "Authorization", "RefreshToken")
        );

        // JWT 필터 설정
        http.addFilterBefore(new JWTFilter(jwtUtil, userRepository), LogoutFilter.class); // 로그아웃 필터전에 jwt필터실행
        http.addFilterBefore(new JWTFilter(jwtUtil, userRepository), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

        // 경로별 인가 작업
        http.securityMatcher("/**") // 모든 요청에 대해
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(WHITE_LIST_URL).permitAll()
                    .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 인증 실패 처리
//                        .accessDeniedHandler(customAuthenticationEntryPoint) // 인가 실패 처리
            );

        // 세션 설정: STATELESS
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
        return http.build();
    }

    private static final String[] WHITE_LIST_URL = {
//            "/api/v1/auth/**",
            "/api/v1/auth/healthcheck",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };
}
