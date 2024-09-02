package com.kakaoteck.golagola.domain.buyer.service;

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

    public static BuyerResponse getMyPage(Buyer buyer) {
        return BuyerResponse.builder()
                .email(buyer.getEmail())
                .role(buyer.getRole())
                .address(buyer.getAddress())
//                .registerDate(buyer.getRegisterDate())
                .realName(buyer.getUsername())
                .gender(buyer.getGender())
                .phoneNum(buyer.getPhoneNum())
                .nickname(buyer.getNickname())
                .build();
    }

    public BuyerResponse updateMyPage(Buyer buyer, BuyerRequest.MyPagePutDto request) {
        buyer.updateProfile(request);
        Buyer savedBuyer = buyerRepository.save(buyer);
        return BuyerResponse.builder()
                .email(savedBuyer.getEmail())
                .role(savedBuyer.getRole())
                .address(savedBuyer.getAddress())
//                .registerDate(savedBuyer.getRegisterDate())
                .realName(savedBuyer.getUsername())
                .gender(savedBuyer.getGender())
                .phoneNum(savedBuyer.getPhoneNum())
                .nickname(savedBuyer.getNickname())
                .build();

    }
}
