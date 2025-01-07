package com.allegroservice.service;

import com.allegroservice.config.AllegroAuthClient;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderService {

    private static final String ORDERS_URL = "https://api.allegro.pl/order/checkout-forms";
    private final AllegroAuthClient authClient;

    public OrderService(AllegroAuthClient authClient) {
        this.authClient = authClient;
    }

    public String fetchOrders() throws IOException {
        String accessToken = authClient.getAccessToken();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ORDERS_URL)
                .get()
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/vnd.allegro.public.v1+json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return response.body().string();
        }
    }
}
