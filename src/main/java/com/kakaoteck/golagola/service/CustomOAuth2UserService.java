package com.kakaoteck.golagola.service;

import com.kakaoteck.golagola.domain.auth.Repository.UserRepository;
import com.kakaoteck.golagola.domain.auth.dto.*;
import com.kakaoteck.golagola.domain.auth.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

// DefaultOAuth2UserService: OAuth2에서 기본적으로 유저를 저장하는 메서드를 가지고 있다.
// super로 상속받아서 사용한다.
// OAuth2UserRequest: 리소스 서버에서 제공되는 유저정보
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest); // 유저 정보

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);

        // 1. 새로운 유저라면
        if (optionalUserEntity.isEmpty()) {

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username); // ex) kakao 3664463254
            userEntity.setEmail(oAuth2Response.getEmail()); // ex) tiger1650@naver.com
            userEntity.setName(oAuth2Response.getName()); // ex) 이용우
            userEntity.setImage(oAuth2Response.getImage()); // ex) 프로필 이미지
            userRepository.save(userEntity);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setEmail(oAuth2Response.getEmail());
//            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }

        // 2. 기존 유러라면
        else {
            UserEntity existData = optionalUserEntity.get();
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());
            existData.setImage(oAuth2Response.getImage());
            userRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
//            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }

    }
}

