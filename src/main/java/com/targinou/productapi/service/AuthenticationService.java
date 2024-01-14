package com.targinou.productapi.service;


import com.targinou.productapi.dto.AuthenticationRequest;
import com.targinou.productapi.dto.AuthenticationResponse;
import com.targinou.productapi.dto.RefreshTokenRequest;
import com.targinou.productapi.model.User;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshAccessToken(RefreshTokenRequest request);

    User getAuthenticatedUser();

}
