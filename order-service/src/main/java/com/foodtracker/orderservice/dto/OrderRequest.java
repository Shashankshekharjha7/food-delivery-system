package com.foodtracker.orderservice.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String customerName;
    private String foodItem;
    private Integer quantity;
}
