package com.kakaoteck.golagola.domain.seller.repository;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
//    Optional<Seller> findByEmail(String email);
//    boolean existsByEmail(String email);

    @Query("SELECT b FROM Seller b WHERE b.user.email = :email")
    Optional<Seller> findByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Seller b WHERE b.user.email = :email")
    boolean existsByEmail(@Param("email") String email);
}
