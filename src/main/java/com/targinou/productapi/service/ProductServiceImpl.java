package com.targinou.productapi.service;

import com.targinou.productapi.dto.ProductDTO;
import com.targinou.productapi.dto.ProductReportRequestDTO;
import com.targinou.productapi.dto.ProductSearchFilterDTO;
import com.targinou.productapi.dto.ProductSummaryDTO;
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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public Page<ProductSummaryDTO> searchSummaryByFilter(ProductSearchFilterDTO searchDTO) {
        var result = productRepository.searchByFilter(searchDTO);

        List<ProductSummaryDTO> dtoList = result.getContent().stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(
                dtoList,
                result.getPageable(),
                result.getTotalElements());
    }

    private ProductSummaryDTO mapToSummaryDTO(Product product) {
        return new ProductSummaryDTO(
                product.getId(),
                product.getName(),
                product.getCostPrice(),
                product.getCostPrice() * product.getStockQuantity(),
                product.getStockQuantity(),
                product.getSalePrice(),
                product.getSalePrice() * product.getStockQuantity()
        );
    }

    public String searchByReportFilter(ProductReportRequestDTO reportRequestDTO, String format) throws IOException {
        Page<Product> result = productRepository.searchByFilter(reportRequestDTO.getSearchFilterDTO());

        List<ProductDTO> dtoList = result.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        if ("CSV".equalsIgnoreCase(format)) {
            return generateCsvFile(dtoList, reportRequestDTO);
        }

        return generateXLSXFile(dtoList, reportRequestDTO);
    }


    private String generateCsvFile(List<ProductDTO> dtoList, ProductReportRequestDTO reportRequestDTO) throws IOException {
        Path csvPath = Files.createTempFile("report", ".csv");
        Map<String, Boolean> fieldsToShow = reportRequestDTO.getFieldsToShow();

        List<String> headers = new ArrayList<>();
        fieldsToShow.forEach((key, value) -> {
            if (value) {
                headers.add(key);
            }
        });

        try (BufferedWriter writer = Files.newBufferedWriter(csvPath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))) {

            for (ProductDTO dto : dtoList) {
                List<Object> data = new ArrayList<>();
                for (String field : headers) {
                    try {
                        String methodName = field.charAt(0) + field.substring(1);
                        Method method = ProductDTO.class.getMethod(methodName);
                        data.add(method.invoke(dto));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                csvPrinter.printRecord(data);
            }
        }

        return csvPath.toString();
    }

    private String generateXLSXFile(List<ProductDTO> dtoList, ProductReportRequestDTO reportRequestDTO) throws IOException {
        Path xlsxPath = Files.createTempFile("report", ".xlsx");

        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fileOut = new FileOutputStream(xlsxPath.toFile())) {
            Sheet sheet = workbook.createSheet("Report");
            Map<String, Boolean> fieldsToShow = reportRequestDTO.getFieldsToShow();
            List<String> headers = new ArrayList<>();

            Row headerRow = sheet.createRow(0);
            int cellIndex = 0;
            for (String field : fieldsToShow.keySet()) {
                if (fieldsToShow.get(field)) {
                    headers.add(field);
                    Cell cell = headerRow.createCell(cellIndex++);
                    cell.setCellValue(field);
                }
            }

            int rowIndex = 1;
            for (ProductDTO dto : dtoList) {
                Row row = sheet.createRow(rowIndex++);
                cellIndex = 0;
                for (String field : headers) {
                    Cell cell = row.createCell(cellIndex++);
                    try {
                        String methodName = field.charAt(0) + field.substring(1);
                        Method method = ProductDTO.class.getMethod(methodName);
                        Object value = method.invoke(dto);
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            workbook.write(fileOut);
        }

        return xlsxPath.toString();
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
