package ru.meschanov.catalogservice.catalogservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.meschanov.catalogservice.catalogservice.dto.OrderCreatedEvent;
import ru.meschanov.catalogservice.catalogservice.repository.ProductRepository;

@Component
@RequiredArgsConstructor
public class OrderConsumer {
    private final ProductRepository productRepository;

    @KafkaListener(topics = "orders", groupId = "catalog-service")
    public void consume(OrderCreatedEvent event) {
        event.getItems().forEach(it -> {
            productRepository.findById(it.getProductId())
                    .ifPresent(product -> {
                        if (product.getQuantityStock() >= it.getQuantity()) {
                            product.setQuantityStock(product.getQuantityStock() - it.getQuantity());
                            productRepository.save(product);
                        } else {
                            throw new IllegalStateException(
                                    "Not enough stock for product " + product.getId()
                            );
                        }
                    });
        });
    }
}
