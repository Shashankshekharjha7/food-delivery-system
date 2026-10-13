package com.foodtracker.kitchenservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "kitchen_tickets")
@Data
public class kitchenTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String foodItem;
    private int quantity;
    private String status;
}
