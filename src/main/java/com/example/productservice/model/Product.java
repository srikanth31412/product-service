package com.example.productservice.model;

import com.example.productservice.domain.event.DomainEvent;
import com.example.productservice.domain.event.ProductDeletedEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_products_name", columnList = "name"),
                @Index(name = "idx_products_sku", columnList = "sku"),
                @Index(name = "idx_products_category", columnList = "category")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = 50, updatable = false)
    private String sku;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, name = "reorder_level")
    private Integer reorderLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /* =========================
       Factory
       ========================= */
    public static Product create(
            String sku,
            String name,
            String description,
            String category,
            BigDecimal price,
            Integer quantity,
            Integer reorderLevel,
            String imageUrl
    ) {
        validatePrice(price);
        validateQuantity(quantity);
        validateReorderLevel(reorderLevel);

        Product product = new Product();
        product.sku = Objects.requireNonNull(sku);
        product.name = Objects.requireNonNull(name);
        product.description = description;
        product.category = Objects.requireNonNull(category);
        product.price = price;
        product.quantity = quantity;
        product.reorderLevel = reorderLevel;
        product.imageUrl = imageUrl;
        product.status = ProductStatus.ACTIVE;

        return product;
    }

    /* =========================
       Domain behavior
       ========================= */

    public void updateBasicDetails(
            String name,
            String description,
            String category,
            Integer reorderLevel,
            String imageUrl
    ) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        if (category != null) this.category = category;
        if (reorderLevel != null) validateReorderLevel(reorderLevel);
        if (reorderLevel != null) this.reorderLevel = reorderLevel;
        if (imageUrl != null) this.imageUrl = imageUrl;
    }

    public void changePrice(BigDecimal newPrice) {
        validatePrice(newPrice);
        this.price = newPrice;
    }

    public void increaseStock(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        this.quantity += amount;
    }

    public void decreaseStock(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (quantity - amount < 0) throw new IllegalStateException("Insufficient stock");
        this.quantity -= amount;
    }

    public boolean isLowStock() {
        return quantity <= reorderLevel;
    }

    public void activate() {
        if (status == ProductStatus.DELETED) {
            throw new IllegalStateException("Deleted product cannot be activated");
        }
        this.status = ProductStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
    }

    public void delete() {
        if (status == ProductStatus.DELETED) return;
        this.status = ProductStatus.DELETED;
        domainEvents.add(new ProductDeletedEvent(this.id));
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }

    /* =========================
       Lifecycle
       ========================= */

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /* =========================
       Validation
       ========================= */

    private static void validatePrice(BigDecimal price) {
        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }
    }

    private static void validateQuantity(Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Quantity must be >= 0");
        }
    }

    private static void validateReorderLevel(Integer reorderLevel) {
        if (reorderLevel == null || reorderLevel < 0) {
            throw new IllegalArgumentException("Reorder level must be >= 0");
        }
    }
}
