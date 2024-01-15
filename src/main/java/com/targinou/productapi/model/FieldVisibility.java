package com.targinou.productapi.model;

import com.targinou.productapi.model.enums.Role;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "field_visibility")
public class FieldVisibility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ElementCollection
    @CollectionTable(name = "visible_fields", joinColumns = @JoinColumn(name = "field_visibility_id"))
    @Column(name = "field_name")
    private Set<String> visibleFields;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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