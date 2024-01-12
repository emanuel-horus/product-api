package com.targinou.productapi.service;

import com.targinou.productapi.dto.ProductDTO;
import com.targinou.productapi.dto.ProductSearchFilterDTO;
import com.targinou.productapi.model.Product;
import org.springframework.data.domain.Page;

public interface ProductService extends GenericService<Product, ProductDTO> {

    Page<ProductDTO> searchByFilter(ProductSearchFilterDTO searchDTO);
}
