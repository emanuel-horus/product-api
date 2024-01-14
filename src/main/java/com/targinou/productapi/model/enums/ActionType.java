package com.targinou.productapi.model.enums;

public enum ActionType {

    CREATION("Criação"),
    CHANGE("Alteração"),
    EXCLUSION("Exlusão");

    private final String description;

    ActionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
