package com.targinou.productapi.model.enums;

public enum Role {

    ADMIN("Administrador"),
    STOCKIST("Estoquista");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
