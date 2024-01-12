package com.targinou.productapi.dto;

import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;

public class ProductSearchFilterDTO {

    private Pageable pageable;
    private Long id;
    private String name;
    private Boolean active;
    private String sku;
    private Long categoryId;
    private Double costPrice;
    private Double icms;
    private Double salePrice;
    private Integer stockQuantity;
    private ZonedDateTime createdAt;
    private String orderBy;
    private String orderDirection;

    public ProductSearchFilterDTO(Pageable pageable,
                                  Long id,
                                  String name,
                                  Boolean active,
                                  String sku,
                                  Long categoryId,
                                  Double costPrice,
                                  Double icms,
                                  Double salePrice,
                                  Integer stockQuantity,
                                  String orderBy,
                                  String orderDirection) {
        this.pageable = pageable;
        this.id = id;
        this.name = name;
        this.active = active;
        this.sku = sku;
        this.categoryId = categoryId;
        this.costPrice = costPrice;
        this.icms = icms;
        this.salePrice = salePrice;
        this.stockQuantity = stockQuantity;
        this.orderBy = orderBy;
        this.orderDirection = orderDirection;
    }


    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public Double getIcms() {
        return icms;
    }

    public void setIcms(Double icms) {
        this.icms = icms;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getOrderBy() {
        return orderBy != null ? orderBy : "id";
    }

    public String getOrderDirection() {
        return orderDirection != null ? orderDirection : "ASC";
    }
}
