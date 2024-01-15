package com.targinou.productapi.controller;

import com.targinou.productapi.dto.ApiResponseDTO;
import com.targinou.productapi.dto.FieldVisibilityDTO;
import com.targinou.productapi.service.FieldVisibilityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/field-visibility")
@SecurityRequirement(name = "bearerAuth")
public class FieldVisibilityController {

    private final FieldVisibilityService fieldVisibilityService;

    public FieldVisibilityController(FieldVisibilityService fieldVisibilityService) {
        this.fieldVisibilityService = fieldVisibilityService;
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponseDTO<Void>> updateVisibilitySettings(@RequestBody FieldVisibilityDTO fieldVisibilityDTO) {
        fieldVisibilityService.updateVisibilitySettings(fieldVisibilityDTO.getRole(), fieldVisibilityDTO.getVisibleFields());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Campos atualizados com sucesso.", null, null));
    }

}
