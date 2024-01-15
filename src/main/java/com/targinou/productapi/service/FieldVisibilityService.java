package com.targinou.productapi.service;

import com.targinou.productapi.model.FieldVisibility;
import com.targinou.productapi.model.enums.Role;
import com.targinou.productapi.repository.FieldVisibilityRepository;
import com.targinou.productapi.utils.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class FieldVisibilityService {

    private final FieldVisibilityRepository fieldVisibilityRepository;
    private final AuthenticationService authenticationService;

    public FieldVisibilityService(FieldVisibilityRepository fieldVisibilityRepository,
                                  AuthenticationService authenticationService) {
        this.fieldVisibilityRepository = fieldVisibilityRepository;
        this.authenticationService = authenticationService;
    }


    public void updateVisibilitySettings(Role role, Set<String> visibleFields) {

        var user = authenticationService.getAuthenticatedUser();

        if (user.getRole() != Role.ADMIN) {
            throw new BusinessException("Você não tem permissão para atualizar essa configuração", HttpStatus.UNAUTHORIZED);
        }

        FieldVisibility visibility = fieldVisibilityRepository.findByRole(role)
                .orElse(new FieldVisibility());
        visibility.setRole(role);
        visibility.setVisibleFields(visibleFields);
        fieldVisibilityRepository.save(visibility);
    }

    public Set<String> getVisibleFieldsForRole(Role role) {
        return fieldVisibilityRepository.findByRole(role)
                .map(FieldVisibility::getVisibleFields)
                .orElse(Collections.emptySet());
    }
}
