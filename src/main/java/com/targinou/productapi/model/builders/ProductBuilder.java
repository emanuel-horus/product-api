package com.targinou.productapi.model.builders;

import com.targinou.productapi.model.Category;
import com.targinou.productapi.model.Product;

public class ProductBuilder {

    private long id;
    private String name;
    private boolean active;
    private String sku;
    private Category category;
    private Double costPrice;
    private Double icms;
    private Double salePrice;
    private String productImage;
    private Integer stockQuantity;

    public ProductBuilder id(long id) {
        this.id = id;
        return this;
    }

    public ProductBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder active(boolean active) {
        this.active = active;
        return this;
    }

    public ProductBuilder sku(String sku) {
        this.sku = sku;
        return this;
    }

    public ProductBuilder category(Category category) {
        this.category = category;
        return this;
    }

    public ProductBuilder costPrice(Double costPrice) {
        this.costPrice = costPrice;
        return this;
    }

    public ProductBuilder icms(Double icms) {
        this.icms = icms;
        return this;
    }

    public ProductBuilder salePrice(Double salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    public ProductBuilder productImage(String productImage) {
        this.productImage = productImage;
        return this;
    }

    public ProductBuilder stockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
        return this;
    }

    public Product build() {
        Product product = new Product();
        product.setId(this.id);
        product.setName(this.name);
        product.setActive(this.active);
        product.setSku(this.sku);
        product.setCategory(this.category);
        product.setCostPrice(this.costPrice);
        product.setIcms(this.icms);
        product.setSalePrice(this.salePrice);
        product.setProductImage(this.productImage);
        product.setStockQuantity(this.stockQuantity);
        return product;
    }
}
