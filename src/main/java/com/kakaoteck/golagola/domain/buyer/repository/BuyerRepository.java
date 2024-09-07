package com.kakaoteck.golagola.domain.buyer.repository;

import com.kakaoteck.golagola.domain.auth.entity.UserEntity;
import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
//    Optional<Buyer> findByEmail(String email);
//    boolean existsByEmail(String email);
    Optional<Buyer> findByUser(UserEntity user);

    @Query("SELECT b FROM Buyer b WHERE b.user.email = :email")
    Optional<Buyer> findByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Buyer b WHERE b.user.email = :email")
    boolean existsByEmail(@Param("email") String email);
}
