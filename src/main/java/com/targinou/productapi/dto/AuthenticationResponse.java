package com.targinou.productapi.dto;

public record AuthenticationResponse(String token, String refreshToken) {
}
