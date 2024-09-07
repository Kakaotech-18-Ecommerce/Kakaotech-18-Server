package com.kakaoteck.golagola.domain.auth.service;

import com.kakaoteck.golagola.domain.auth.Repository.UserRepository;
import com.kakaoteck.golagola.domain.auth.dto.AuthRequest;
import com.kakaoteck.golagola.domain.auth.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService1 {

    private final UserRepository userRepository;

    @Transactional
    public void saveUserDetails(String username, AuthRequest authRequest) {
        // 1. username으로 해당 유저가 존재하는지 확인
        userRepository.findByUsername(username).ifPresentOrElse(user -> {
            // 2. 유저가 존재하면 닉네임과 성별을 업데이트
            userRepository.updateUserInfo(username, authRequest.nickName(), authRequest.gender());
        }, () -> {
            // 3. 존재하지 않을 경우 예외 처리 또는 다른 로직 수행
            throw new IllegalArgumentException("User with username " + username + " not found.");
        });
    }

    public void saveUser(UserEntity userEntity) {
        userRepository.save(userEntity); // UserEntity를 저장하면 Buyer/Seller도 함께 저장됨
    }



}
