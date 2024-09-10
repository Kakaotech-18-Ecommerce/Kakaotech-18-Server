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
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService1 authService;

    @Operation(summary = "AWS healthcheck", description = "헬스체크 api")
    @GetMapping("/healthcheck")
    public ApiResponse<String> healthcheck(){
        return ApiResponse.onSuccess("로그인 성공");
    }

    @Operation(summary = "로그인 됐는지 확인하기", description = "JWT필터 통과 시 로그인 성공")
    @GetMapping("/logincheck")
    public ApiResponse<String> logincheck(){
        return ApiResponse.onSuccess("로그인 성공");
    }

    @Operation(summary = "회원가입 추가정보 진행", description = "(nickname, gender, role) 저장")
    @PostMapping("/join")
    public ApiResponse<String> join(@RequestBody AuthRequest authRequest, @AuthenticationPrincipal CustomOAuth2User customUser) {

        // 1. UserEntity 가져오기
        UserEntity userEntity = customUser.getUserEntity();

        // 2. 유저 정보 업데이트 (닉네임, 성별, 전화번호 등)
        userEntity.setNickname(authRequest.nickName());
        userEntity.setGender(authRequest.gender());
        userEntity.setPhoneNum(authRequest.phoneNumber());
        userEntity.setRole(authRequest.role());

        UserDTO updatedUserDTO = customUser.getUserDTO();
        updatedUserDTO.setNickname(authRequest.nickName());
        updatedUserDTO.setGender(authRequest.gender());

        // 3. Role에 따른 Buyer 또는 Seller 객체 생성
        if (Role.BUYER == authRequest.role()) {

            Buyer buyer = Buyer.builder()
                    .user(userEntity)
                    .roadAddress(authRequest.roadAddress())
                    .zipCode(authRequest.zipCode())
                    .detailAdress(authRequest.detailAdress())
                    .build();
            userEntity.setBuyer(buyer);  // UserEntity에 Buyer 설정
        } else if (Role.SELLER == authRequest.role()) {
            Seller seller = Seller.builder()
                    .user(userEntity)
                    .roadAddress(authRequest.roadAddress())
                    .zipCode(authRequest.zipCode())
                    .detailAdress(authRequest.detailAdress())
                    .build();
            userEntity.setSeller(seller);  // UserEntity에 Seller 설정
        } else {
            return ApiResponse.onFailure("Invalid role");
        }

        // 4. 업데이트된 UserEntity 저장 (Cascade 옵션으로 인해 Buyer/Seller도 저장됨)
        authService.saveUser(userEntity);

        return ApiResponse.onSuccess("회원가입 성공");
    }



}
