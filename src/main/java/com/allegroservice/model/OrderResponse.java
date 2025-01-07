package com.allegroservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("buyer")
    private Buyer buyer;

    @JsonProperty("status")
    private String status;

    @JsonProperty("items")
    private List<OrderItem> items;

    @Data
    public static class Buyer {
        @JsonProperty("id")
        private String id;

        @JsonProperty("login")
        private String login;
    }

    @Data
    public static class OrderItem {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("quantity")
        private int quantity;

        @JsonProperty("price")
        private Price price;

        @Data
        public static class Price {
            @JsonProperty("amount")
            private String amount;

            @JsonProperty("currency")
            private String currency;
        }
    }
}
