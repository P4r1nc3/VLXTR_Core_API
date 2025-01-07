package com.allegroservice.controller;

import com.allegroservice.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String getOrders() {
        System.out.println("GET /orders");
        try {
            return orderService.fetchOrders();
        } catch (Exception e) {
            return "Error fetching orders: " + e.getMessage();
        }
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("test");
        return "test";
    }
}
