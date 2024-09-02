//package com.kakaoteck.golagola.domain.auth.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.kakaoteck.golagola.domain.auth.dto.AuthRequest;
//import com.kakaoteck.golagola.domain.auth.dto.AuthResponse;
//import com.kakaoteck.golagola.domain.auth.dto.JoinUserRequest;
//import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
//import com.kakaoteck.golagola.domain.buyer.repository.BuyerRepository;
//import com.kakaoteck.golagola.domain.seller.entity.Seller;
//import com.kakaoteck.golagola.domain.seller.repository.SellerRepository;
//import com.kakaoteck.golagola.global.common.code.status.ErrorStatus;
//import com.kakaoteck.golagola.global.common.enums.Gender;
//import com.kakaoteck.golagola.global.common.enums.Role;
//import com.kakaoteck.golagola.global.common.exception.GeneralException;
//import com.kakaoteck.golagola.security.service.JwtService;
//import com.kakaoteck.golagola.security.token.Token;
//import com.kakaoteck.golagola.security.token.TokenBlackListRepository;
//import com.kakaoteck.golagola.security.token.TokenRepository;
//import com.kakaoteck.golagola.security.token.enums.TokenType;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//    private final BuyerRepository buyerRepository;
//    private final SellerRepository sellerRepository;
//    private final TokenRepository tokenRepository;
//    private final TokenBlackListRepository tokenBlackListRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;
//
//    public void register(JoinUserRequest request) {
//        if ("BUYER".equals(request.role())) {
//            Buyer buyer = Buyer.builder()
//                    .email(request.email())
//                    .nickname(request.nickname())
//                    .username(request.realName())
//                    .phoneNum(request.phoneNum())
//                    .gender(Gender.valueOf(request.gender()))
//                    .role(Role.valueOf(request.role()))
//                    .address(request.address())
//                    .build();
//            buyerRepository.save(buyer);
//        } else if ("SELLER".equals(request.role())) {
//            Seller seller = Seller.builder()
//                    .email(request.email())
//                    .nickname(request.nickname())
//                    .username(request.realName())
//                    .phoneNum(request.phoneNum())
//                    .gender(Gender.valueOf(request.gender()))
//                    .address(request.address())
//                    .role(Role.valueOf(request.role()))
//                    .build();
//            sellerRepository.save(seller);
//        }
//    }
//
//    public AuthResponse authenticate(AuthRequest request) {
//        System.out.println("Attempting authentication for: " + request.email());
//
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.email(),
//                            request.password()
//                    )
//            );
//        } catch (AuthenticationException e) {
//            System.out.println("Authentication failed for email: " + request.email());
//            throw new GeneralException(ErrorStatus._LOGIN_USER_INVALID);
//        }
//
//        Buyer buyer = buyerRepository.findByEmail(request.email()).orElse(null);
//        Seller seller = sellerRepository.findByEmail(request.email()).orElse(null);
//
//        if (buyer == null && seller == null) {
//            throw new GeneralException(ErrorStatus._INVALID_USER);
//        }
//
//        System.out.println("Authentication successful for: " + request.email());
//
//        // Determine the UserDetails type and generate tokens
//        UserDetails user = buyer != null ? buyer : seller;
//        String jwtToken = jwtService.generateToken(user);
//        String refreshToken = jwtService.generateRefreshToken(user);
//
//        revokeAllUserTokens(user);
//        saveUserToken(user, jwtToken);
//
//        return AuthResponse.builder()
//                .accessToken(jwtToken)
//                .refreshToken(refreshToken)
//                .build();
//    }
//
//    private void saveUserToken(UserDetails user, String jwtToken) {
//        Token token = Token.builder()
//                .token(jwtToken)
//                .tokenType(TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .build();
//
//        if (user instanceof Buyer) {
//            token.setBuyer((Buyer) user);
//        } else if (user instanceof Seller) {
//            token.setSeller((Seller) user);
//        }
//
//        tokenRepository.save(token);
//    }
//
//    private void revokeAllUserTokens(UserDetails user) {
//        if (user instanceof Buyer) {
//            var validUserTokens = tokenRepository.findAllValidTokenByBuyer(((Buyer) user).getId());
//            validUserTokens.forEach(token -> {
//                token.setExpired(true);
//                token.setRevoked(true);
//            });
//            tokenRepository.saveAll(validUserTokens);
//        } else if (user instanceof Seller) {
//            var validUserTokens = tokenRepository.findAllValidTokenBySeller(((Seller) user).getId()); // .getSellerId() 원래 이거임
//            validUserTokens.forEach(token -> {
//                token.setExpired(true);
//                token.setRevoked(true);
//            });
//            tokenRepository.saveAll(validUserTokens);
//        }
//    }
//
//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        String refreshToken = authHeader.substring(7);
//        if (tokenBlackListRepository.existsByToken(refreshToken)) {
//            throw new NoSuchElementException("유효하지 않은 접근입니다.");
//        }
//
//        String userEmail = jwtService.extractUserName(refreshToken);
//        if (userEmail != null) {
//            var buyerOptional = buyerRepository.findByEmail(userEmail);
//            var sellerOptional = sellerRepository.findByEmail(userEmail);
//
//            if (buyerOptional.isPresent()) {
//                processTokenRefresh(buyerOptional.get(), refreshToken, response);
//            } else if (sellerOptional.isPresent()) {
//                processTokenRefresh(sellerOptional.get(), refreshToken, response);
//            } else {
//                throw new NoSuchElementException("User not found for email: " + userEmail);
//            }
//        }
//    }
//
//    private void processTokenRefresh(UserDetails user, String refreshToken, HttpServletResponse response) throws IOException {
//        if (jwtService.isTokenValid(refreshToken, user)) {
//            String accessToken = jwtService.generateToken(user);
//            revokeAllUserTokens(user);
//            saveUserToken(user, accessToken);
//
//            AuthResponse authResponse = AuthResponse.builder()
//                    .accessToken(accessToken)
//                    .refreshToken(refreshToken)
//                    .build();
//            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//        }
//    }
//
//    public boolean checkEmailExists(String email) {
//        return buyerRepository.existsByEmail(email) || sellerRepository.existsByEmail(email);
//    }
//}
