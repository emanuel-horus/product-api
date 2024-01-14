package com.targinou.productapi.service;

import com.targinou.productapi.dto.ProductDTO;
import com.targinou.productapi.dto.ProductReportRequestDTO;
import com.targinou.productapi.dto.ProductSearchFilterDTO;
import com.targinou.productapi.dto.ProductSummaryDTO;
import com.targinou.productapi.model.Product;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface ProductService extends GenericService<Product, ProductDTO> {

    Page<ProductDTO> searchByFilter(ProductSearchFilterDTO searchDTO);

    Page<ProductSummaryDTO> searchSummaryByFilter(ProductSearchFilterDTO searchDTO);

    String searchByReportFilter(ProductReportRequestDTO reportRequestDTO, String format) throws IOException;
}
