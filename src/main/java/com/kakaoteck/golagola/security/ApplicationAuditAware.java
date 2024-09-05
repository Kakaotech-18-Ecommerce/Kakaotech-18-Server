package com.kakaoteck.golagola.security;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Seller) {
            Seller seller = (Seller) principal;
            return Optional.ofNullable(seller.getSellerId());
        } else if (principal instanceof Buyer) {
            Buyer buyer = (Buyer) principal;
            return Optional.ofNullable(buyer.getBuyerId());
        } else {
            return Optional.empty();
        }
    }
}