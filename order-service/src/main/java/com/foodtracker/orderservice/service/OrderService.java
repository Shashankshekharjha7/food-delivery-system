package com.foodtracker.orderservice.service;

import com.foodtracker.orderservice.dto.OrderRequest;
import com.foodtracker.orderservice.entity.Order;
import com.foodtracker.orderservice.event.OrderPlacedEvent;
import com.foodtracker.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Transactional
    public void placeOrder(OrderRequest request){
        // 1. Create and save the order entity
        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .foodItems(request.getFoodItem()) // Entity uses plural
                .quantity(request.getQuantity())
                .status("PENDING")
                .build();

        Order savedOrder = orderRepository.save(order);
        log.info("Order saved to database with ID: {}", savedOrder.getId());

        // 2. Create the Kafka event (mapped perfectly for the Kitchen Service)
        OrderPlacedEvent event = new OrderPlacedEvent(
                savedOrder.getId(),
                savedOrder.getCustomerName(),
                savedOrder.getFoodItems(),
                savedOrder.getQuantity()
        );

        // 3. Publish to Kafka and FORCE a wait for confirmation
        try {
            kafkaTemplate.send("order-events", event).get(); // .get() blocks until Kafka replies
            log.info("OrderPlacedEvent successfully ACKNOWLEDGED by Kafka for Order ID: {}", savedOrder.getId());
        } catch (Exception e) {
            log.error("🚨 CRITICAL: FAILED TO SEND MESSAGE TO KAFKA 🚨", e);
            throw new RuntimeException("Kafka send failed!", e);
        }
    }
}