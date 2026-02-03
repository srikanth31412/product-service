package com.example.productservice.domain.event;

import java.time.LocalDateTime;

public record ProductDeletedEvent(Long productId) implements DomainEvent {

    @Override
    public LocalDateTime occurredAt() {
        return LocalDateTime.now();
    }
}
