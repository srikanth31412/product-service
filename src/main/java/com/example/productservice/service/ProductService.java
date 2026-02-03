package com.example.productservice.service;

import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.UpdateProductRequest;
import com.example.productservice.model.Product;
import com.example.productservice.model.ProductStatus;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /* =========================
       CREATE
       ========================= */
    @Transactional
    public ProductDTO createProduct(CreateProductRequest request) {
        productRepository.findBySku(request.sku())
                .ifPresent(p -> {
                    throw new IllegalStateException(
                            "Product with SKU already exists: " + request.sku()
                    );
                });

        Product product = Product.create(
                request.sku(),
                request.name(),
                request.description(),
                request.category(),
                request.price(),
                request.quantity(),
                request.reorderLevel(),
                request.imageUrl()
        );

        return toDTO(productRepository.save(product));
    }

    /* =========================
       READ
       ========================= */
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        return toDTO(findById(id));
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductBySku(String sku) {
        return toDTO(
                productRepository.findBySku(sku)
                        .orElseThrow(() ->
                                new IllegalStateException("Product not found with SKU: " + sku)
                        )
        );
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategory(String category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getLowStockProducts() {
        return productRepository.findLowStockProducts()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String searchTerm) {
        return productRepository.searchProducts(searchTerm)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /* =========================
       UPDATE
       ========================= */
    @Transactional
    public ProductDTO updateProduct(Long id, UpdateProductRequest request) {
        Product product = findById(id);

        if (request.price() != null) {
            product.changePrice(request.price());
        }

        if (request.quantity() != null) {
            reconcileInventory(product, request.quantity());
        }

        if (request.status() != null) {
            if (request.status() == ProductStatus.ACTIVE) {
                product.activate();
            } else {
                product.deactivate();
            }
        }

        product.updateBasicDetails(
                request.name(),
                request.description(),
                request.category(),
                request.reorderLevel(),
                request.imageUrl()
        );

        return toDTO(productRepository.save(product));
    }

    /* =========================
       INVENTORY
       ========================= */
    @Transactional
    public ProductDTO increaseInventory(Long id, int amount) {
        Product product = findById(id);
        product.increaseStock(amount);
        return toDTO(productRepository.save(product));
    }

    @Transactional
    public ProductDTO decreaseInventory(Long id, int amount) {
        Product product = findById(id);
        product.decreaseStock(amount);
        return toDTO(productRepository.save(product));
    }

    @Transactional
    public ProductDTO updateInventory(Long id, int newQuantity) {
        Product product = findById(id);
        reconcileInventory(product, newQuantity);
        return toDTO(productRepository.save(product));
    }

    /* =========================
       DELETE
       ========================= */
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository
                .findByIdAndStatusNot(id, ProductStatus.DELETED)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.delete(); // domain logic
    }

    /* =========================
       INTERNAL HELPERS
       ========================= */
    private Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException("Product not found with id: " + id)
                );
    }

    private void reconcileInventory(Product product, int newQuantity) {
        int current = product.getQuantity();

        if (newQuantity > current) {
            product.increaseStock(newQuantity - current);
        } else if (newQuantity < current) {
            product.decreaseStock(current - newQuantity);
        }
    }

    private ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getPrice(),
                product.getQuantity(),
                product.getReorderLevel(),
                product.getStatus(),
                product.getImageUrl(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
