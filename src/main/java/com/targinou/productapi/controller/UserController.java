package com.targinou.productapi.controller;


import com.targinou.productapi.dto.ApiResponseDTO;
import com.targinou.productapi.dto.UserDTO;
import com.targinou.productapi.dto.enums.RoleDTO;
import com.targinou.productapi.model.User;
import com.targinou.productapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@Validated
public class UserController extends GenericController<User, UserDTO, UserService> {
    protected UserController(UserService service) {
        super(service);
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponseDTO<List<RoleDTO>>> getAllRoles() {
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "All available roles.", service.getAllRoles(), null));
    }

}
