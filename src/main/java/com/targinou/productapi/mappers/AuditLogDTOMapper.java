package com.targinou.productapi.mappers;

import com.targinou.productapi.dto.AuditLogDTO;
import com.targinou.productapi.dto.ProductDTO;
import com.targinou.productapi.dto.UserDTO;
import com.targinou.productapi.model.AuditLog;
import com.targinou.productapi.model.Product;
import com.targinou.productapi.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuditLogDTOMapper implements DtoMapper<AuditLog, AuditLogDTO> {

    private final ProductDTOMapper productDTOMapper;
    private final UserDTOMapper userDTOMapper;

    public AuditLogDTOMapper(ProductDTOMapper productDTOMapper, UserDTOMapper userDTOMapper) {
        this.productDTOMapper = productDTOMapper;
        this.userDTOMapper = userDTOMapper;
    }

    @Override
    public AuditLogDTO toDto(AuditLog entity) {

        ProductDTO productDTO = entity.getProduct() != null ? productDTOMapper.toDto(entity.getProduct()) : null;
        UserDTO userDTO = entity.getUser()!= null ? userDTOMapper.toDto(entity.getUser()) : null;

        assert productDTO != null;
        assert userDTO != null;
        return new AuditLogDTO(
                entity.getId(),
                productDTO.id(),
                userDTO.id(),
                entity.getActionType(),
                entity.getDescription()
        );
    }

    @Override
    public AuditLog toEntity(AuditLogDTO dto) {

        var product = new Product();
        product.setId(dto.id());

        var user = new User();
        user.setId(dto.userId());

        AuditLog auditLog = new AuditLog();
        auditLog.setId(dto.id());
        auditLog.setProduct(product);
        auditLog.setActionType(dto.actionType());
        auditLog.setUser(user);
        auditLog.setDescription(dto.description());

        return auditLog;
    }

}
