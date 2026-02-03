package com.example.productservice.controller;

import com.example.productservice.dto.*;
import com.example.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    /* =========================
       CREATE
       ========================= */
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductDTO product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    /* =========================
       READ
       ========================= */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductDTO> getProductBySku(@PathVariable String sku) {
        return ResponseEntity.ok(productService.getProductBySku(sku));
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<ProductDTO> products = productService.getAllProducts(PageRequest.of(page, size));
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable String category) {
        List<ProductDTO> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam("q") String query) {
        List<ProductDTO> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/inventory/low-stock")
    public ResponseEntity<List<ProductDTO>> getLowStockProducts() {
        List<ProductDTO> products = productService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }

    /* =========================
       UPDATE
       ========================= */
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @PutMapping("/{id}/inventory")
    public ResponseEntity<ProductDTO> updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInventoryRequest request
    ) {
        return ResponseEntity.ok(productService.updateInventory(id, request.quantity()));
    }

    @PostMapping("/{id}/inventory/increase")
    public ResponseEntity<ProductDTO> increaseInventory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInventoryRequest request
    ) {
        return ResponseEntity.ok(productService.increaseInventory(id, request.quantity()));
    }

    @PostMapping("/{id}/inventory/decrease")
    public ResponseEntity<ProductDTO> decreaseInventory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInventoryRequest request
    ) {
        return ResponseEntity.ok(productService.decreaseInventory(id, request.quantity()));
    }

    /* =========================
       DELETE
       ========================= */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
