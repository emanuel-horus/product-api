package com.targinou.productapi.service;

import com.targinou.productapi.dto.ProductDTO;
import com.targinou.productapi.dto.ProductSearchFilterDTO;
import com.targinou.productapi.mappers.CategoryDTOMapper;
import com.targinou.productapi.mappers.DtoMapper;
import com.targinou.productapi.mappers.ProductDTOMapper;
import com.targinou.productapi.model.AuditLog;
import com.targinou.productapi.model.Product;
import com.targinou.productapi.model.enums.ActionType;
import com.targinou.productapi.model.enums.Role;
import com.targinou.productapi.repository.AuditLogRepository;
import com.targinou.productapi.repository.GenericRepository;
import com.targinou.productapi.repository.ProductRepository;
import com.targinou.productapi.utils.exceptions.BusinessException;
import com.targinou.productapi.utils.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final AuthenticationService authenticationService;
    private final AuditLogService auditLogService;
    private final AuditLogRepository auditLogRepository;
    private final ProductDTOMapper mapper;
    private final CategoryDTOMapper categoryDTOMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                              AuthenticationService authenticationService,
                              AuditLogService auditLogService,
                              AuditLogRepository auditLogRepository,
                              ProductDTOMapper mapper,
                              CategoryDTOMapper categoryDTOMapper) {
        this.productRepository = productRepository;
        this.authenticationService = authenticationService;
        this.auditLogService = auditLogService;
        this.auditLogRepository = auditLogRepository;
        this.mapper = mapper;
        this.categoryDTOMapper = categoryDTOMapper;
    }

    @Override
    public GenericRepository<Product> getRepository() {
        return productRepository;
    }

    @Override
    public DtoMapper<Product, ProductDTO> getDtoMapper() {
        return mapper;
    }

    @Override
    public Page<ProductDTO> searchByFilter(ProductSearchFilterDTO searchDTO) {
        var result = productRepository.searchByFilter(searchDTO);

        List<ProductDTO> dtoList = result.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(
                dtoList,
                result.getPageable(),
                result.getTotalElements());
    }

    @Override
    public ProductDTO create(ProductDTO dto) {
        Product entity = getDtoMapper().toEntity(dto);
        var currentUser = authenticationService.getAuthenticatedUser();
        entity.setUser(currentUser);
        validateBeforeSave(entity);
        Product savedEntity = getRepository().save(entity);

        var auditLog = new AuditLog(savedEntity, ActionType.CREATION, currentUser, "Produto criado");
        auditLogRepository.save(auditLog);

        return getDtoMapper().toDto(savedEntity);
    }


    @Override
    public ProductDTO update(Long id, ProductDTO dto) {
        Product existingEntity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found: " + id));

        var currentUser = authenticationService.getAuthenticatedUser();

        if (currentUser.getRole() == Role.STOCKIST) {
            if (!Objects.equals(dto.costPrice(), existingEntity.getCostPrice()) ||
                    !Objects.equals(dto.icms(), existingEntity.getIcms())) {
                throw new BusinessException("Estoquistas não estão autorizados a alterar o Valor de Custo ou ICMS.", HttpStatus.FORBIDDEN);
            }
        }

        String description = auditLogService.buildUpdateDescription(existingEntity, getDtoMapper().toEntity(dto));

        Product updatedEntity = getDtoMapper().toEntity(dto);
        updatedEntity.setId(id);

        if (dto.name() != null) {
            existingEntity.setName(dto.name());
        }
        if (dto.sku() != null) {
            existingEntity.setSku(dto.sku());
        }
        if (dto.category() != null) {
            existingEntity.setCategory(categoryDTOMapper.toEntity(dto.category()));
        }
        if (dto.costPrice() != null) {
            existingEntity.setCostPrice(dto.costPrice());
        }
        if (dto.icms() != null) {
            existingEntity.setIcms(dto.icms());
        }
        if (dto.salePrice() != null) {
            existingEntity.setSalePrice(dto.salePrice());
        }
        if (dto.productImage() != null) {
            existingEntity.setProductImage(dto.productImage());
        }
        if (dto.stockQuantity() != null) {
            existingEntity.setStockQuantity(dto.stockQuantity());
        }

        validateBeforeUpdate(existingEntity);
        existingEntity = getRepository().save(existingEntity);

        var auditLog = new AuditLog(existingEntity, ActionType.CHANGE, currentUser, description);
        auditLogRepository.save(auditLog);

        return getDtoMapper().toDto(existingEntity);
    }

    @Override
    public void deleteById(Long id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found: " + id));

        if (!entity.isActive()) {
            throw new BusinessException("Produto já excluído anteriormente.", HttpStatus.BAD_REQUEST);
        }

        entity.setActive(false);
        getRepository().delete(entity);

        var currentUser = authenticationService.getAuthenticatedUser();

        var auditLog = new AuditLog(entity, ActionType.EXCLUSION, currentUser, "Produto excluído");
        auditLogRepository.save(auditLog);
    }

}
