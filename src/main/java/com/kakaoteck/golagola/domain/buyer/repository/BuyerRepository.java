package com.kakaoteck.golagola.domain.buyer.repository;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    Optional<Buyer> findByEmail(String email);
    boolean existsByEmail(String email);
}
