package com.targinou.productapi.repository;

import com.targinou.productapi.dto.ProductSearchFilterDTO;
import com.targinou.productapi.model.Product;
import org.springframework.data.domain.Page;

public interface ProductCustomRepository {
    Page<Product> searchByFilter(ProductSearchFilterDTO searchDTO);
}
