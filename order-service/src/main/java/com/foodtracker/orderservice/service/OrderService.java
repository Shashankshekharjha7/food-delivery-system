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
        //1. Create and save the order entity
        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .foodItems(request.getFoodItem())
                .quantity(request.getQuantity())
                .status("PENDING")
                .build();

        Order savedOrder = orderRepository.save(order);
        log.info("Order saved to database with ID: {}", savedOrder.getId());

        //2. Create the kafka event
        OrderPlacedEvent event = new OrderPlacedEvent(
                savedOrder.getId(),
                savedOrder.getFoodItems(),
                savedOrder.getQuantity()
        );

        //3. PUblish to kafka topic 'order-events'
        kafkaTemplate.send("order-events", event);
        log.info("OrderPlacedEvent pushed to kafka for Order ID: {}", savedOrder.getId());
    }
}
