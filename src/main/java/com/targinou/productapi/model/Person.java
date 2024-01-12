package com.targinou.productapi.model;

import com.targinou.productapi.model.builders.PersonBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Entity
@Table
@Where(clause = "active = true")
public class Person extends BaseEntity {

    @Column(name = "name")
    @NotBlank(message = "O nome é obrigatório.")
    private String name;

    @Column(name = "identifier", nullable = false)
    @NotBlank(message = "O identificador é obrigatório.")
    private String identifier;

    @Column(name = "email")
    @Email(message = "O email deve ser válido.")
    @NotBlank(message = "O email é obrigatório.")
    private String email;

    @Column(name = "phone_number")
    @NotBlank(message = "O número de telefone é obrigatório.")
    private String phoneNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    public static PersonBuilder builder() {
        return new PersonBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
