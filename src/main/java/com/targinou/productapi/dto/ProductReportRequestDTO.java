package com.targinou.productapi.dto;

import java.util.Map;

public class ProductReportRequestDTO {
    private Map<String, Boolean> fieldsToShow;
    private ProductSearchFilterDTO searchFilterDTO;

    public Map<String, Boolean> getFieldsToShow() {
        return fieldsToShow;
    }

    public void setFieldsToShow(Map<String, Boolean> fieldsToShow) {
        this.fieldsToShow = fieldsToShow;
    }

    public ProductSearchFilterDTO getSearchFilterDTO() {
        return searchFilterDTO;
    }

    public void setSearchFilterDTO(ProductSearchFilterDTO searchFilterDTO) {
        this.searchFilterDTO = searchFilterDTO;
    }
}