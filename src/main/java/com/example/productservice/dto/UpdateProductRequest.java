package com.example.productservice.dto;

import com.example.productservice.model.ProductStatus;
import java.math.BigDecimal;

import com.example.productservice.model.ProductStatus;
import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,
        String description,
        String category,
        BigDecimal price,
        Integer reorderLevel,
        String imageUrl,
        ProductStatus status,
        Integer quantity
) {
    public static class Builder {
        private String name;
        private String description;
        private String category;
        private BigDecimal price;
        private Integer reorderLevel;
        private String imageUrl;
        private ProductStatus status;
        private Integer quantity;

        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder price(BigDecimal price) { this.price = price; return this; }
        public Builder reorderLevel(Integer reorderLevel) { this.reorderLevel = reorderLevel; return this; }
        public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public Builder status(ProductStatus status) { this.status = status; return this; }
        public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }

        public UpdateProductRequest build() {
            return new UpdateProductRequest(name, description, category, price, reorderLevel, imageUrl, status, quantity);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}


