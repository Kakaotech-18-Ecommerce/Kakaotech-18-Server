package com.kakaoteck.golagola.domain.auth.Repository;

import com.kakaoteck.golagola.domain.auth.entity.UserEntity;
import com.kakaoteck.golagola.global.common.enums.Gender;
import com.kakaoteck.golagola.global.common.enums.Role;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
//    UserEntity findByUsername(String username); // username을 전달하여 해당하는 엔티티 가져오기(JPA)

    Optional<UserEntity> findByUsername(String username); // 차이가 뭔지 공부하기

    @Modifying
    @Query("UPDATE UserEntity u SET u.refreshToken = :refreshToken, u.loginStatus = :loginStatus WHERE u.username = :username")
    void updateRefreshTokenAndLoginStatus(@Param("userName") String username,
                                          @Param("refreshToken") String refreshToken,
                                          @Param("loginStatus") boolean loginStatus);

    @Modifying
    @Query("UPDATE UserEntity u SET u.nickname = :nickName, u.gender = :gender WHERE u.username = :username")

    int updateUserInfo(@Param("username") String username,
                       @Param("nickName") String nickName,
                       @Param("gender") Gender gender);

}



