package com.kakaoteck.golagola.domain.seller.service;

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

    public SellerResponse getMyPage(Seller seller) {
        return SellerResponse.builder()
                .email(seller.getEmail())
                .role(seller.getRole())
                .address(seller.getAddress())
//                .registerDate(seller.getRegisterDate())
                .realName(seller.getUsername())
                .gender(seller.getGender())
                .phoneNum(seller.getPhoneNum())
                .nickname(seller.getNickname())
                .build();
    }

    public SellerResponse updateMyPage(Seller seller, SellerRequest.MyPagePutDto request) {
        seller.updateProfile(request);
        Seller savedSeller = sellerRepository.save(seller);
        return SellerResponse.builder()
                .email(savedSeller.getEmail())
                .role(savedSeller.getRole())
                .address(savedSeller.getAddress())
//                .registerDate(savedSeller.getRegisterDate())
                .realName(savedSeller.getUsername())
                .gender(savedSeller.getGender())
                .phoneNum(savedSeller.getPhoneNum())
                .nickname(savedSeller.getNickname())
                .build();
    }
}
