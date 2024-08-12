package com.kakaoteck.golagola.domain.seller.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.kakaoteck.golagola.global.common.BaseEntity;
import com.kakaoteck.golagola.global.common.enums.Gender;
import com.kakaoteck.golagola.global.common.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Seller extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;

    private String nickname;
    private Gender gender;
    private String email;
    private String address;
    private String phoneNum;
    private Role role;
    private String realName;
    private LocalDate registerDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
