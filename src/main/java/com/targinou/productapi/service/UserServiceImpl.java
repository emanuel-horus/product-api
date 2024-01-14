package com.targinou.productapi.service;


import com.targinou.productapi.dto.UserDTO;
import com.targinou.productapi.dto.enums.RoleDTO;
import com.targinou.productapi.mappers.DtoMapper;
import com.targinou.productapi.mappers.UserDTOMapper;
import com.targinou.productapi.model.User;
import com.targinou.productapi.model.enums.Role;
import com.targinou.productapi.repository.GenericRepository;
import com.targinou.productapi.repository.UserRepository;
import com.targinou.productapi.utils.exceptions.BusinessException;
import com.targinou.productapi.utils.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserDTOMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, UserDTOMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public GenericRepository<User> getRepository() {
        return this.repository;
    }

    @Override
    public DtoMapper<User, UserDTO> getDtoMapper() {
        return this.mapper;
    }


    @Override
    public List<RoleDTO> getAllRoles() {
        return Arrays.stream(Role.values())
                .map(role -> new RoleDTO(role.name(), role.getDescription()))
                .toList();
    }

    @Override
    public UserDTO update(Long id, UserDTO userDTO) {
        if (id != userDTO.id()) {
            throw new BusinessException("O id não pode ser alterado!", HttpStatus.BAD_REQUEST);
        }
        User userDb = getRepository().findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found: " + id));

        var updatedEntity = getDtoMapper().toEntity(userDTO);
        updatedEntity.setId(id);
        updatedEntity.getPerson().setId(userDb.getPerson().getId());
        updatedEntity.setPassword(userDb.getPassword());
        validateBeforeUpdate(updatedEntity);
        checkLoginBeforeUpdate(userDb.getLogin(), updatedEntity.getLogin());
        checkEmailBeforeUpdate(userDb.getPerson().getEmail(), updatedEntity.getPerson().getEmail());
        getRepository().save(updatedEntity);

        return getDtoMapper().toDto(getRepository().save(updatedEntity));
    }

    private void checkLoginBeforeUpdate(String loginUserDb, String loginUpdatedUser) {
        if (!Objects.equals(loginUserDb, loginUpdatedUser)) {
            validateLogin(loginUpdatedUser);
        }
    }

        private void checkEmailBeforeUpdate(String emailUserDb, String emailUpdatedUser) {
        if (!Objects.equals(emailUserDb, emailUpdatedUser)) {
            validateEmail(emailUpdatedUser);
        }
    }


    @Override
    public void validateBeforeSave(User entity) {
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        UserService.super.validateBeforeSave(entity);
        validateLogin(entity.getLogin());
        validateEmail(entity.getPerson().getEmail());
    }

    private void validateLogin(String login) {
        if (repository.existsByLogin(login)) {
            throw new BusinessException("Login inválido: " + login + ". Já existe um usuário cadastrado com esse identificador", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateEmail(String email) {
        if (repository.existsPersonByEmail(email)) {
            throw new BusinessException("Email inválido: " + email + ". Já existe um usuário cadastrado com esse email", HttpStatus.BAD_REQUEST);
        }
    }

}
