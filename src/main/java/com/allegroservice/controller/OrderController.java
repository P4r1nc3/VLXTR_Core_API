package com.allegroservice.controller;

import com.allegroservice.dto.OrderResponse;
import com.allegroservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getOrders() {
        List<OrderResponse> orders = orderService.fetchOrders();
        return ResponseEntity.ok(orders);
    }
}
