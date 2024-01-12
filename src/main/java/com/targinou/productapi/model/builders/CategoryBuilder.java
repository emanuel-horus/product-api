package com.targinou.productapi.model.builders;

import com.targinou.productapi.model.Category;
import com.targinou.productapi.model.enums.Type;

public class CategoryBuilder {

    private long id;
    private String name;
    private boolean active;
    private Type type;

    public CategoryBuilder id(long id) {
        this.id = id;
        return this;
    }

    public CategoryBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder active(boolean active) {
        this.active = active;
        return this;
    }

    public CategoryBuilder type(Type type) {
        this.type = type;
        return this;
    }

    public Category build() {
        Category category = new Category();
        category.setId(this.id);
        category.setName(this.name);
        category.setActive(this.active);
        category.setType(this.type);
        return category;
    }
}
