package com.targinou.productapi.dto;

import com.targinou.productapi.model.enums.Role;

import java.util.Set;

public class FieldVisibilityDTO {
    private Role role;
    private Set<String> visibleFields;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<String> getVisibleFields() {
        return visibleFields;
    }

    public void setVisibleFields(Set<String> visibleFields) {
        this.visibleFields = visibleFields;
    }
}

