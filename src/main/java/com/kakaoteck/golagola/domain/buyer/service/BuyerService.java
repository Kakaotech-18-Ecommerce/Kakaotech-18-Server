package com.kakaoteck.golagola.domain.buyer.service;

import com.kakaoteck.golagola.domain.auth.Repository.UserRepository;
import com.kakaoteck.golagola.domain.auth.entity.UserEntity;
import com.kakaoteck.golagola.domain.buyer.dto.BuyerRequest;
import com.kakaoteck.golagola.domain.buyer.dto.BuyerResponse;
import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.buyer.repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BuyerService {

    private final BuyerRepository buyerRepository;
    private final UserRepository userRepository;

    public static BuyerResponse getMyPage(Buyer buyer) {
//        return BuyerResponse.builder()
//                .email(userEntity.getEmail())
//                .role(userEntity.getRole())
//                .address(userEntity.getBuyer().getAddress())
//                .realName(userEntity.getName())
//                .gender(userEntity.getGender())
//                .phoneNum(userEntity.getPhoneNum())
//                .nickname(userEntity.getNickname())
//                .build();
        return BuyerResponse.builder()
                .email(buyer.getUser().getEmail())
                .role(buyer.getUser().getRole())
                .address(buyer.getAddress())
                .realName(buyer.getUser().getName())
                .gender(buyer.getUser().getGender())
                .phoneNum(buyer.getUser().getPhoneNum())
                .nickname(buyer.getUser().getNickname())
                .build();
    }

//    public BuyerResponse updateMyPage(@AuthenticationPrincipal CustomOAuth2User customUser, BuyerRequest.MyPagePutDto request) {
//        Buyer buyer = buyerRepository.findByUser(userEntity)
//                .orElseThrow(() -> new IllegalArgumentException("Buyer not found"));
//
//        buyer.updateProfile(request);
//
//        // 3. Buyer 및 UserEntity 정보 저장
//        buyerRepository.save(buyer);
//        userRepository.save(userEntity);
//
//        return BuyerResponse.builder()
//                .email(userEntity.getEmail())
//                .role(userEntity.getRole())
//                .address(userEntity.getBuyer().getAddress())
//                .realName(userEntity.getName())
//                .gender(userEntity.getGender())
//                .phoneNum(userEntity.getPhoneNum())
//                .nickname(userEntity.getNickname())
//                .build();
//    }
public BuyerResponse updateMyPage(Buyer buyer, BuyerRequest.MyPagePutDto request) {
    buyer.updateProfile(request);

    // 3. Buyer 및 UserEntity 정보 저장
    buyerRepository.save(buyer);
    userRepository.save(buyer.getUser());

    return BuyerResponse.builder()
            .email(buyer.getUser().getEmail())
            .role(buyer.getUser().getRole())
            .address(buyer.getAddress())
            .realName(buyer.getUser().getName())
            .gender(buyer.getUser().getGender())
            .phoneNum(buyer.getUser().getPhoneNum())
            .nickname(buyer.getUser().getNickname())
            .build();
}
}
