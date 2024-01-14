package com.targinou.productapi.mappers;

import com.targinou.productapi.dto.AuditLogDTO;
import com.targinou.productapi.dto.ProductDTO;
import com.targinou.productapi.dto.UserDTO;
import com.targinou.productapi.model.AuditLog;
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

        return new AuditLogDTO(
                entity.getId(),
                productDTO,
                userDTO,
                entity.getActionType(),
                entity.getDescription()
        );
    }

    @Override
    public AuditLog toEntity(AuditLogDTO dto) {
        AuditLog auditLog = new AuditLog();
        auditLog.setId(dto.id());
        auditLog.setProduct(dto.product() != null ? productDTOMapper.toEntity(dto.product()) : null);
        auditLog.setActionType(dto.actionType());
        auditLog.setUser(dto.user() != null ? userDTOMapper.toEntity(dto.user()) : null);
        auditLog.setDescription(dto.description());

        return auditLog;
    }

}
