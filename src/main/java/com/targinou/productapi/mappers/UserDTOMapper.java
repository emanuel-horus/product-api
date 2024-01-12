package com.targinou.productapi.mappers;


import com.targinou.productapi.dto.UserDTO;
import com.targinou.productapi.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper implements DtoMapper<User, UserDTO> {

    @Override
    public UserDTO toDto(User entity) {
        return new UserDTO(
                entity.getId(),
                new PersonDTOMapper().toDto(entity.getPerson()),
                entity.getLogin(),
                entity.getPassword(),
                entity.getRole()
        );
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.id())
                .person(new PersonDTOMapper().toEntity(userDTO.person()))
                .login(userDTO.login())
                .password(userDTO.password())
                .role(userDTO.role())
                .build();
    }
}
