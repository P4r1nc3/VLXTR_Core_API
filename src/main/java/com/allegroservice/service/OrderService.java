package com.allegroservice.service;

import com.allegroservice.model.OrderResponse;
import com.allegroservice.model.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class OrderService {

    private final TokenService tokenService;
    private final WebClient webClient;

    public OrderService(TokenService tokenService, WebClient.Builder webClientBuilder) {
        this.tokenService = tokenService;
        this.webClient = webClientBuilder.baseUrl("https://api.allegro.pl").build();
    }

    public List<OrderResponse> fetchOrders() {
        TokenResponse tokenResponse = tokenService.fetchAccessToken();

        return webClient.get()
                .uri("/order/checkout-forms") // Endpoint zamówień
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .header("Accept", "application/vnd.allegro.public.v1+json")
                .retrieve()
                .bodyToFlux(OrderResponse.class)
                .collectList()
                .block();
    }
}
