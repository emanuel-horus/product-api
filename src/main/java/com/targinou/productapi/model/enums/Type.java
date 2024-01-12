package com.targinou.productapi.model.enums;

public enum Type {

    NORMAL("Normal"),
    ESPECIAL("Especial"),
    CUSTOM("Personalizado");

    private final String description;

    Type(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
