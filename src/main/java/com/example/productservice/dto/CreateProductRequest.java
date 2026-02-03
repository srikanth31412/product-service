package com.example.productservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Objects;

public record CreateProductRequest(
        @NotBlank
        String sku,
        String name,
        String description,
        String category,
        BigDecimal price,
        Integer quantity,
        Integer reorderLevel,
        String imageUrl
) {
    // Builder
    public static class Builder {
        private String sku;
        private String name;
        private String description;
        private String category;
        private BigDecimal price;
        private Integer quantity;
        private Integer reorderLevel;
        private String imageUrl;

        public Builder sku(String sku) { this.sku = sku; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder price(BigDecimal price) { this.price = price; return this; }
        public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public Builder reorderLevel(Integer reorderLevel) { this.reorderLevel = reorderLevel; return this; }
        public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }

        public CreateProductRequest build() {
            // Optional: add validation for required fields
            Objects.requireNonNull(sku, "SKU is required");
            Objects.requireNonNull(name, "Name is required");
            Objects.requireNonNull(category, "Category is required");
            Objects.requireNonNull(price, "Price is required");
            Objects.requireNonNull(quantity, "Quantity is required");
            Objects.requireNonNull(reorderLevel, "Reorder level is required");

            return new CreateProductRequest(
                    sku, name, description, category, price, quantity, reorderLevel, imageUrl
            );
        }
    }

    // Convenience method
    public static Builder builder() {
        return new Builder();
    }
}


