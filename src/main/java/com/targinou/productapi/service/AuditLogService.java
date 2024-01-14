package com.targinou.productapi.service;

import com.targinou.productapi.dto.AuditLogDTO;
import com.targinou.productapi.model.AuditLog;
import com.targinou.productapi.model.Product;

public interface AuditLogService extends GenericService<AuditLog, AuditLogDTO>{

    String buildUpdateDescription(Product existingEntity, Product updatedEntity);
}
