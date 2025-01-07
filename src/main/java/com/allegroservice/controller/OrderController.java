package com.allegroservice.controller;

import com.allegroservice.model.OrderResponse;
import com.allegroservice.service.OrderService;
import com.allegroservice.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    private final TokenService tokenService;
    private final OrderService orderService;

    public OrderController(TokenService tokenService, OrderService orderService) {
        this.tokenService = tokenService;
        this.orderService = orderService;
    }

    @GetMapping("/token")
    public ResponseEntity<?> getToken() {
        return ResponseEntity.ok(tokenService.fetchAccessToken());
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getOrders() {
        List<OrderResponse> orders = orderService.fetchOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
