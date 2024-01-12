package com.targinou.productapi.controller;


import com.targinou.productapi.dto.ApiResponseDTO;
import com.targinou.productapi.dto.AuthenticationRequest;
import com.targinou.productapi.dto.AuthenticationResponse;
import com.targinou.productapi.dto.RefreshTokenRequest;
import com.targinou.productapi.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponseDTO<AuthenticationResponse>> register(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        true,
                        "Authentication completed successfully",
                        response,
                        null
                ));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponseDTO<AuthenticationResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        AuthenticationResponse response = authenticationService.refreshAccessToken(request);
        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        true,
                        "Token refreshed successfully",
                        response,
                        null
                ));
    }

}
