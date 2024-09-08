package com.kakaoteck.golagola.domain.buyer.service;

import com.kakaoteck.golagola.domain.auth.Repository.UserRepository;
import com.kakaoteck.golagola.domain.auth.entity.UserEntity;
import com.kakaoteck.golagola.domain.buyer.dto.BuyerRequest;
import com.kakaoteck.golagola.domain.buyer.dto.BuyerResponse;
import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.buyer.repository.BuyerRepository;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
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

public BuyerResponse updateMyPage(Buyer buyer, BuyerRequest.MyPagePutDto request) {
    buyer.updateProfile(request);

    // Buyer 및 UserEntity 정보 저장
    Buyer savedBuyer = buyerRepository.save(buyer);
    userRepository.save(buyer.getUser());

    return BuyerResponse.builder()
            .email(savedBuyer.getUser().getEmail())
            .role(savedBuyer.getUser().getRole())
            .address(savedBuyer.getAddress())
            .realName(savedBuyer.getUser().getName())
            .gender(savedBuyer.getUser().getGender())
            .phoneNum(savedBuyer.getUser().getPhoneNum())
            .nickname(savedBuyer.getUser().getNickname())
            .build();
    }
}
