package com.targinou.productapi.service;


import com.targinou.productapi.dto.AuthenticationRequest;
import com.targinou.productapi.dto.AuthenticationResponse;
import com.targinou.productapi.dto.RefreshTokenRequest;
import com.targinou.productapi.repository.UserRepository;
import com.targinou.productapi.security.jwt.JwtService;
import com.targinou.productapi.utils.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     JwtService jwtService,
                                     AuthenticationManager authenticationManager,
                                     UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.login(), request.password()));
        }catch (BadCredentialsException exception){
            throw new BusinessException("Credenciais inválidas!", HttpStatus.BAD_REQUEST);
        }

        var user = userRepository.findByLogin(request.login()).orElseThrow();
        return jwtService.generateToken(user, user.getId());
    }

    public AuthenticationResponse refreshAccessToken(RefreshTokenRequest request) {
        try {
            var username = jwtService.extractUsername(request.refreshToken());
            var userDetails = userDetailsService.loadUserByUsername(username);
            var user = userRepository.findByLogin(username).orElseThrow();

            if (!jwtService.isTokenValid(request.refreshToken(), userDetails)) {
                throw new BusinessException("Refresh token inválido ou expirado!", HttpStatus.FORBIDDEN);
            }

            return jwtService.generateToken(userDetails, user.getId());

        } catch (Exception e) {
            throw new BusinessException("Erro ao gerar novo token de acesso: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
