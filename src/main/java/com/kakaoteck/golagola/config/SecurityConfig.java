package com.kakaoteck.golagola.config;

import com.kakaoteck.golagola.jwt.JWTFilter;
import com.kakaoteck.golagola.jwt.JWTUtil;
import com.kakaoteck.golagola.oauth2.CustomSuccessHandler;
import com.kakaoteck.golagola.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

        @Override
        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

            CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setMaxAge(3600L);

            configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
            configuration.setExposedHeaders(Collections.singletonList("Authorization"));

            return configuration;
            }
        }));

        // CSRF 보호 비활성화
        http.csrf(csrf -> csrf.disable());

        // 폼 로그인 비활성화
        http.formLogin(login -> login.disable());

        // HTTP Basic 인증 비활성화
        http.httpBasic(basic -> basic.disable());

        //JWTFilter 추가
//        http.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 재로그인 방지를 위한 JWTFilter 선행해서 실행
        http.addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);


        //oauth2
        http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                .userService(customOAuth2UserService))
                .successHandler(customSuccessHandler)
        );

        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
            .requestMatchers("/").permitAll()
            .anyRequest().authenticated()); // 나머지 주소는 인증

        //세션 설정 : STATELESS
        http.sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


}