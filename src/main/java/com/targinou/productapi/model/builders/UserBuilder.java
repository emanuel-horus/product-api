package com.targinou.productapi.model.builders;


import com.targinou.productapi.model.Person;
import com.targinou.productapi.model.User;
import com.targinou.productapi.model.enums.Role;

public class UserBuilder {

    private long id;
    private Person person;
    private String login;
    private String password;
    private Role role;


    public UserBuilder id(long id) {
        this.id = id;
        return this;
    }

    public UserBuilder login(String login) {
        this.login = login;
        return this;
    }

    public UserBuilder person(Person person) {
        this.person = person;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder role(Role role) {
        this.role = role;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(this.id);
        user.setLogin(this.login);
        user.setPassword(this.password);
        user.setRole(this.role);
        user.setPerson(this.person);
        return user;
    }
}
