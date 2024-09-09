package com.kakaoteck.golagola.domain.seller.service;

import com.kakaoteck.golagola.domain.auth.Repository.UserRepository;
import com.kakaoteck.golagola.domain.seller.dto.SellerRequest;
import com.kakaoteck.golagola.domain.seller.dto.SellerResponse;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.domain.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;

    public SellerResponse getMyPage(Seller seller) {
        return SellerResponse.builder()
                .email(seller.getUser().getEmail())
                .role(seller.getUser().getRole())
                .roadAddress(seller.getRoadAddress())
                .zipCode(seller.getZipCode())
                .detailAdress(seller.getDetailAdress())
                .realName(seller.getUser().getName())
                .gender(seller.getUser().getGender())
                .phoneNum(seller.getUser().getPhoneNum())
                .nickname(seller.getUser().getNickname())
                .build();

    }

    public SellerResponse updateMyPage(Seller seller, SellerRequest.MyPagePutDto request) {
        seller.updateProfile(request);

        // Seller 및 UserEntity 정보 저장
        Seller savedSeller = sellerRepository.save(seller);
        userRepository.save(seller.getUser());

        return SellerResponse.builder()
                .email(savedSeller.getUser().getEmail())
                .role(savedSeller.getUser().getRole())
                .roadAddress(savedSeller.getRoadAddress())
                .zipCode(savedSeller.getZipCode())
                .detailAdress(savedSeller.getDetailAdress())
                .realName(savedSeller.getUser().getName())
                .gender(savedSeller.getUser().getGender())
                .phoneNum(savedSeller.getUser().getPhoneNum())
                .nickname(savedSeller.getUser().getNickname())
                .build();
    }
}
