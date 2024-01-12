package com.targinou.productapi.model;

import com.targinou.productapi.model.builders.CategoryBuilder;
import com.targinou.productapi.model.enums.Type;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {


    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    public static CategoryBuilder builder() {
        return new CategoryBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
