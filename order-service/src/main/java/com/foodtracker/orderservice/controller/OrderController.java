package com.foodtracker.orderservice.controller;

import com.foodtracker.orderservice.dto.OrderRequest;
import com.foodtracker.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@RequestBody OrderRequest request){
        orderService.placeOrder(request);
        return "Order submitted successfully to the kitchen!";
    }
}
