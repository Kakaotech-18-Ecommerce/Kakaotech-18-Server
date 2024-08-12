package com.kakaoteck.golagola.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
            select t from Token t inner join Buyer u on t.buyer.buyerId = u.buyerId
            where u.buyerId = :id and (t.expired = false or t.revoked = false)
            """)
    List<Token> findAllValidTokenByBuyer(@Param("id") Long id);

    @Query(value = """
            select t from Token t inner join Seller u on t.seller.sellerId = u.sellerId
            where u.sellerId = :id and (t.expired = false or t.revoked = false)
            """)
    List<Token> findAllValidTokenBySeller(@Param("id") Long id);

    Optional<Token> findByToken(String token);
}
