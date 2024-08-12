package com.kakaoteck.golagola.config;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.buyer.repository.BuyerRepository;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.domain.seller.repository.SellerRepository;
import com.kakaoteck.golagola.security.ApplicationAuditAware;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Try to find the user as a Buyer
            Buyer buyer = buyerRepository.findByEmail(username)
                    .orElse(null);

            if (buyer != null) {
                return new org.springframework.security.core.userdetails.User(
                        buyer.getEmail(),
                        buyer.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority("BUYER"))
                );
            }

            // Try to find the user as a Seller
            Seller seller = sellerRepository.findByEmail(username)
                    .orElse(null);

            if (seller != null) {
                return new org.springframework.security.core.userdetails.User(
                        seller.getEmail(),
                        seller.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority("SELLER"))
                );
            }

            throw new UsernameNotFoundException("User not found with username: " + username);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuditorAware<Long> auditorAware() {
        return new ApplicationAuditAware();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}