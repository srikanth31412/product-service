package com.example.productservice.dto;

public record UpdateInventoryRequest(
        Long productId,
        Integer quantity,
        String reason
) {
    public static class Builder {
        private Long productId;
        private Integer quantity;
        private String reason;

        public Builder productId(Long productId) { this.productId = productId; return this; }
        public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public Builder reason(String reason) { this.reason = reason; return this; }

        public UpdateInventoryRequest build() {
            return new UpdateInventoryRequest(productId, quantity, reason);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
