package com.kakaoteck.golagola.Repository;

import com.kakaoteck.golagola.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username); // username을 전달하여 해당하는 엔티티 가져오기(JPA)
}



