package com.targinou.productapi.dto;

import java.time.LocalDate;

public record PersonDTO(
        long id,
        String name,
        String identifier,
        String email,
        String phoneNumber,
        LocalDate birthDate
) {
}
