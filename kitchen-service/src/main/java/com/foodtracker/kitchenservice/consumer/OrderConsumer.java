package com.foodtracker.kitchenservice.consumer;

import com.foodtracker.kitchenservice.dto.OrderEvent;
import com.foodtracker.kitchenservice.entity.kitchenTicket;
import com.foodtracker.kitchenservice.repository.kitchenTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final kitchenTicketRepository ticketRepository;

    @KafkaListener(topics = "order-events", groupId = "kitchen-group-v2")
    public void consumeOrder(OrderEvent event) {
        log.info("📥 Order event received: {}", event);

        // Map to database entity
        kitchenTicket ticket = new kitchenTicket();
        ticket.setCustomerName(event.getCustomerName());
        ticket.setFoodItem(event.getFoodItem());
        ticket.setQuantity(event.getQuantity());
        ticket.setStatus("PREPARING");

        // Save to PostgreSQL
        ticketRepository.save(ticket);

        System.out.println("=================================");
        System.out.println("🚨 NEW KITCHEN TICKET RECEIVED! 🚨");
        System.out.println("Customer: " + event.getCustomerName());
        System.out.println("Order: " + event.getQuantity() + "x " + event.getFoodItem());
        System.out.println("Status: Chef is preparing the food...");
        System.out.println("======================================");
    }
}