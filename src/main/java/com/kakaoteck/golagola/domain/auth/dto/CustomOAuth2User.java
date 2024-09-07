package com.kakaoteck.golagola.domain.auth.dto;

import com.kakaoteck.golagola.domain.auth.entity.UserEntity;
import com.kakaoteck.golagola.global.common.enums.Gender;
import com.kakaoteck.golagola.global.common.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements UserDetails, OAuth2User{

    private final UserDTO userDTO;
    private UserEntity userEntity; // UserEntity

    // UserDTO만을 받는 생성자 추가 (UserEntity가 필요하지 않은 경우)
    public CustomOAuth2User(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    // UserDTO와 UserEntity를 함께 받는 생성자
    public CustomOAuth2User(UserDTO userDTO, UserEntity userEntity) {
        this.userDTO = userDTO;
        this.userEntity = userEntity;
    }

    // UserDTO 객체를 반환하는 getter 메서드 추가
    public UserDTO getUserDTO() {
        return this.userDTO;
    }

    // UserEntity를 반환하는 getter 메서드 추가
    public UserEntity getUserEntity() {
        return this.userEntity;
    }

    @Override
    public String getPassword() {
        return "";
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

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //Role값을 구현해주는 형태

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userDTO.getRole();
            }
        });
        return collection;
    }
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // Role에 따라 권한을 설정하기 위해 getAuthoritiesForRole 메서드를 호출
//        return getAuthoritiesForRole(userEntity.getRole());
//    }

    public Collection<? extends GrantedAuthority> getAuthoritiesForRole(Role role) {
        if (role == Role.BUYER) {
            return List.of(new SimpleGrantedAuthority("ROLE_BUYER"));
        } else if (role == Role.SELLER) {
            return List.of(new SimpleGrantedAuthority("ROLE_SELLER"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));  // 기본 권한
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }

    public String getUsername() {
        return userDTO.getUsername();
    }

    public Long getId() {  // id 값을 반환하는 메서드 추가
        return userDTO.getId();
    }

    public String getNickname(){
        return userDTO.getNickname();
    }

    public Gender getGender(){
        return userDTO.getGender();
    }

}
