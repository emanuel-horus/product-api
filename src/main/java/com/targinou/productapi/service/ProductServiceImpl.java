package com.targinou.productapi.service;

import com.targinou.productapi.dto.ProductDTO;
import com.targinou.productapi.dto.ProductSearchFilterDTO;
import com.targinou.productapi.mappers.DtoMapper;
import com.targinou.productapi.mappers.ProductDTOMapper;
import com.targinou.productapi.model.Product;
import com.targinou.productapi.repository.GenericRepository;
import com.targinou.productapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDTOMapper mapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductDTOMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
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

}
