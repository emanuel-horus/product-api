package com.targinou.productapi.service;


import com.targinou.productapi.dto.AuditLogDTO;
import com.targinou.productapi.mappers.AuditLogDTOMapper;
import com.targinou.productapi.mappers.DtoMapper;
import com.targinou.productapi.model.AuditLog;
import com.targinou.productapi.model.Product;
import com.targinou.productapi.repository.AuditLogRepository;
import com.targinou.productapi.repository.GenericRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Transactional
@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository repository;
    private final AuditLogDTOMapper mapper;

    public AuditLogServiceImpl(AuditLogRepository repository,
                               AuditLogDTOMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public GenericRepository<AuditLog> getRepository() {
        return this.repository;
    }

    @Override
    public DtoMapper<AuditLog, AuditLogDTO> getDtoMapper() {
        return mapper;
    }

    public String buildUpdateDescription(Product existingEntity, Product updatedEntity) {
        StringBuilder description = new StringBuilder();

        if (!Objects.equals(existingEntity.getName(), updatedEntity.getName())) {
            appendUpdate(description, "Nome", existingEntity.getName(), updatedEntity.getName());
        }

        if (!Objects.equals(existingEntity.getSku(), updatedEntity.getSku())) {
            appendUpdate(description, "SKU", existingEntity.getSku(), updatedEntity.getSku());
        }

        if (!Objects.equals(existingEntity.getCostPrice(), updatedEntity.getCostPrice())) {
            appendUpdate(description, "Preço de Custo", existingEntity.getCostPrice(), updatedEntity.getCostPrice());
        }

        if (!Objects.equals(existingEntity.getIcms(), updatedEntity.getIcms())) {
            appendUpdate(description, "ICMS", existingEntity.getIcms(), updatedEntity.getIcms());
        }

        if (!Objects.equals(existingEntity.getSalePrice(), updatedEntity.getSalePrice())) {
            appendUpdate(description, "Preço de Venda", existingEntity.getSalePrice(), updatedEntity.getSalePrice());
        }

        if (!Objects.equals(existingEntity.getProductImage(), updatedEntity.getProductImage())) {
            appendUpdate(description, "Imagem do Produto", existingEntity.getProductImage(), updatedEntity.getProductImage());
        }

        if (!Objects.equals(existingEntity.getStockQuantity(), updatedEntity.getStockQuantity())) {
            appendUpdate(description, "Quantidade em Estoque", existingEntity.getStockQuantity(), updatedEntity.getStockQuantity());
        }

        return description.toString();
    }

    private void appendUpdate(StringBuilder description, String field, Object oldValue, Object newValue) {
        if (description.length() > 0) {
            description.append(",\n");
        }

        description.append("Campo: ").append(field).append(" - Valor anterior: ").append(oldValue).append(" - Valor atual: ").append(newValue);
    }

}
