package com.targinou.productapi.service;


import com.targinou.productapi.dto.AuthenticationRequest;
import com.targinou.productapi.dto.AuthenticationResponse;
import com.targinou.productapi.dto.RefreshTokenRequest;
import com.targinou.productapi.model.RefreshToken;
import com.targinou.productapi.model.User;
import com.targinou.productapi.repository.RefreshTokenRepository;
import com.targinou.productapi.repository.UserRepository;
import com.targinou.productapi.security.jwt.JwtService;
import com.targinou.productapi.utils.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     JwtService jwtService,
                                     AuthenticationManager authenticationManager,
                                     UserDetailsService userDetailsService,
                                     RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.login(), request.password()));
        }catch (BadCredentialsException exception){
            throw new BusinessException("Credenciais inválidas!", HttpStatus.BAD_REQUEST);
        }

        var user = userRepository.findByLogin(request.login()).orElseThrow();
        refreshTokenRepository.updateIsUsedByUserId(user.getId());

        var response = jwtService.generateToken(user, user.getId());

        refreshTokenRepository.save(new RefreshToken(response.refreshToken(), user));

        return response;
    }

    @Transactional
    public AuthenticationResponse refreshAccessToken(RefreshTokenRequest request) {
        try {
            var username = jwtService.extractUsername(request.refreshToken());
            var userDetails = userDetailsService.loadUserByUsername(username);
            var user = userRepository.findByLogin(username).orElseThrow();
            var refreshToken = refreshTokenRepository.findByToken(request.refreshToken());

            if (!jwtService.isTokenValid(request.refreshToken(), userDetails) || refreshToken.isEmpty() || refreshToken.get().isIsUsed()) {
                throw new BusinessException("Refresh token inválido ou expirado!", HttpStatus.FORBIDDEN);
            }

            refreshTokenRepository.updateIsUsedByUserId(user.getId());
            var response = jwtService.generateToken(userDetails, user.getId());
            refreshTokenRepository.save(new RefreshToken(response.refreshToken(), user));

            return response;
        } catch (Exception e) {
            throw new BusinessException("Erro ao gerar novo token de acesso: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("Usuário não autenticado!", HttpStatus.UNAUTHORIZED);
        }

        String username = authentication.getName();
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado!", HttpStatus.NOT_FOUND));
    }

}
