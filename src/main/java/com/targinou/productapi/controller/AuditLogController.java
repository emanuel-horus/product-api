package com.targinou.productapi.controller;


import com.targinou.productapi.dto.AuditLogDTO;
import com.targinou.productapi.model.AuditLog;
import com.targinou.productapi.service.AuditLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/audit")
@Validated
public class AuditLogController extends GenericController<AuditLog, AuditLogDTO, AuditLogService> {
    protected AuditLogController(AuditLogService service) {
        super(service);
    }


}
