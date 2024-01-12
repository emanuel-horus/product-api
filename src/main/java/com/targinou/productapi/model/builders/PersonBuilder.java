package com.targinou.productapi.model.builders;

import com.targinou.productapi.model.Person;

import java.time.LocalDate;

public class PersonBuilder {
    private long id;
    private String name;
    private String identifier;

    private String email;
    private String phoneNumber;
    private LocalDate birthDate;

    public PersonBuilder id(long id) {
        this.id = id;
        return this;
    }

    public PersonBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public PersonBuilder email(String email) {
        this.email = email;
        return this;
    }

    public PersonBuilder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public PersonBuilder birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public Person build() {
        Person person = new Person();
        person.setId(this.id);
        person.setName(this.name);
        person.setEmail(this.email);
        person.setIdentifier(this.identifier);
        person.setPhoneNumber(this.phoneNumber);
        person.setBirthDate(this.birthDate);
        return person;
    }

}
