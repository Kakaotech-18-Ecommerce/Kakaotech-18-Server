package com.kakaoteck.golagola.domain.auth.controller;


import com.kakaoteck.golagola.domain.auth.dto.AuthRequest;
import com.kakaoteck.golagola.domain.auth.dto.CustomOAuth2User;
import com.kakaoteck.golagola.domain.auth.dto.UserDTO;
import com.kakaoteck.golagola.domain.auth.entity.UserEntity;
import com.kakaoteck.golagola.domain.auth.service.AuthService1;
import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.global.common.ApiResponse;
import com.kakaoteck.golagola.global.common.enums.Role;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // 해당하는 유저에다가 추가적인 정보 저장하기

//    @Operation(summary = "회원가입 추가정보 진행", description = "(nickname, gender) 저장")
//    @PostMapping("/join")
//    public ApiResponse<String> join(@RequestBody AuthRequest authRequest, @AuthenticationPrincipal CustomOAuth2User customUser) {
//        String username = customUser.getUsername();
//
//        // 1. UserService를 통해 (nickname, gender) 저장
//        authService.saveUserDetails(username, authRequest);
//
//        // 2. 기존 UserDTO를 업데이트
//        UserDTO updatedUserDTO = customUser.getUserDTO();
//        updatedUserDTO.setNickname(authRequest.nickName());
//        updatedUserDTO.setGender(authRequest.gender());
//
//
//        // 3. CustomOAuth2User 객체 업데이트 (UserEntity 유지)
//        UserEntity userEntity = customUser.getUserEntity(); // 기존 UserEntity 유지
//        CustomOAuth2User updatedCustomOAuth2User = new CustomOAuth2User(updatedUserDTO, userEntity);
//
//        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedCustomOAuth2User, null, updatedCustomOAuth2User.getAuthorities());
//
//        // 5. SecurityContextHolder에 새로운 Authentication 객체로 업데이트
//        SecurityContextHolder.getContext().setAuthentication(newAuth);
//
//        return ApiResponse.onSuccess("회원가입 성공");
//    }

    @Operation(summary = "회원가입 추가정보 진행", description = "(nickname, gender, role) 저장")
    @PostMapping("/join")
    public ApiResponse<String> join(@RequestBody AuthRequest authRequest, @AuthenticationPrincipal CustomOAuth2User customUser) {
        String username = customUser.getUsername();

        // 1. UserEntity 가져오기
        UserEntity userEntity = customUser.getUserEntity();

        // 2. 유저 정보 업데이트 (닉네임, 성별, 전화번호 등)
        userEntity.setNickname(authRequest.nickName());
        userEntity.setGender(authRequest.gender());
        userEntity.setPhoneNum(authRequest.phoneNumber());
        userEntity.setRole(authRequest.role());

        // 5. SecurityContextHolder에 새로운 Authentication 객체로 업데이트
        UserDTO updatedUserDTO = customUser.getUserDTO();
        updatedUserDTO.setNickname(authRequest.nickName());
        updatedUserDTO.setGender(authRequest.gender());

        // 3. Role에 따른 Buyer 또는 Seller 객체 생성
        if (Role.BUYER == authRequest.role()) {

            Buyer buyer = Buyer.builder()
                    .user(userEntity)
                    .address(authRequest.address())
                    .build();
            userEntity.setBuyer(buyer);  // UserEntity에 Buyer 설정
//        } else if ("SELLER".equalsIgnoreCase(authRequest.role())) {
//            Seller seller = Seller.builder()
//                    .user(userEntity)
//                    .businessName(authRequest.address()) // Seller의 추가 정보
//                    .role(Role.SELLER)
//                    .build();
//            userEntity.setSeller(seller);  // UserEntity에 Seller 설정
//            Authentication newAuth = new UsernamePasswordAuthenticationToken(seller, null, customUser.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(newAuth);
        } else {
            return ApiResponse.onFailure("Invalid role");
        }

        // 4. 업데이트된 UserEntity 저장 (Cascade 옵션으로 인해 Buyer/Seller도 저장됨)
        authService.saveUser(userEntity);

//        CustomOAuth2User updatedCustomOAuth2User = new CustomOAuth2User(updatedUserDTO, userEntity);
//        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedCustomOAuth2User, null, updatedCustomOAuth2User.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return ApiResponse.onSuccess("회원가입 성공");
    }

}
