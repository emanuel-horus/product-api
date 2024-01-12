package com.targinou.productapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.targinou.productapi.model.enums.Role;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(
        long id,
        PersonDTO person,
        String login,
        String password,
        Role role

) implements EntityDTO {

    @Override
    public UserDTO toResponse() {
        return new UserDTO(this.id(), this.person(), this.login(), null, this.role());
    }
}
