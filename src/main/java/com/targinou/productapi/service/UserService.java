package com.targinou.productapi.service;


import com.targinou.productapi.dto.UserDTO;
import com.targinou.productapi.dto.enums.RoleDTO;
import com.targinou.productapi.model.User;

import java.util.List;

public interface UserService extends GenericService<User, UserDTO> {

    List<RoleDTO> getAllRoles();

}
