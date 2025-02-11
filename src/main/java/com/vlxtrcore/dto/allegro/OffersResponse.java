package com.vlxtrcore.dto.allegro;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OffersResponse {
    private List<Offer> offers;
    private int count;
    private int totalCount;

    @Data
    @NoArgsConstructor
    public static class Offer {
        private String id; // Offer ID
        private String name; // Offer name
        private List<ProductSet> productSet; // List of products linked to the offer
        private List<Parameter> parameters; // Offer parameters
        private List<String> images; // Image URLs
        private AfterSalesServices afterSalesServices; // After-sales services information
        private Payments payments; // Payment details
        private SellingMode sellingMode; // Selling mode details
        private Stock stock; // Stock details
        private Location location; // Location details
        private Delivery delivery; // Delivery details
        private Description description; // Description details
        private Category category; // Category information
        private Validation validation; // Validation details
        private String language; // Language of the offer
        private Publication publication; // Publication status and details
        private String createdAt; // Creation date
        private String updatedAt; // Last updated date

        @Data
        @NoArgsConstructor
        public static class ProductSet {
            private Product product;
            private Quantity quantity;

            @Data
            @NoArgsConstructor
            public static class Product {
                private String id;
                private Publication publication;
                private List<Parameter> parameters;
            }

            @Data
            @NoArgsConstructor
            public static class Quantity {
                private int value;
            }
        }

        @Data
        @NoArgsConstructor
        public static class Parameter {
            private String id;
            private String name;
            private List<String> values;
            private List<String> valuesIds;
            private String rangeValue;
        }

        @Data
        @NoArgsConstructor
        public static class AfterSalesServices {
            private Object impliedWarranty;
            private Object returnPolicy;
            private Object warranty;
        }

        @Data
        @NoArgsConstructor
        public static class Payments {
            private String invoice;
        }

        @Data
        @NoArgsConstructor
        public static class SellingMode {
            private String format;
            private Price price;
            private Price startingPrice;
            private Price minimalPrice;

            @Data
            @NoArgsConstructor
            public static class Price {
                private String amount;
                private String currency;
            }
        }

        @Data
        @NoArgsConstructor
        public static class Stock {
            private int available;
            private String unit;
        }

        @Data
        @NoArgsConstructor
        public static class Location {
            private String countryCode;
            private String province;
            private String city;
            private String postCode;
        }

        @Data
        @NoArgsConstructor
        public static class Delivery {
            private ShippingRates shippingRates;
            private String handlingTime;
            private String additionalInfo;
            private String shipmentDate;

            @Data
            @NoArgsConstructor
            public static class ShippingRates {
                private String id;
            }
        }

        @Data
        @NoArgsConstructor
        public static class Description {
            private List<Section> sections;

            @Data
            @NoArgsConstructor
            public static class Section {
                private List<Item> items;

                @Data
                @NoArgsConstructor
                public static class Item {
                    private String type; // "TEXT" or "IMAGE"
                    private String content; // Text content for "TEXT" type
                    private String url; // Image URL for "IMAGE" type
                }
            }
        }

        @Data
        @NoArgsConstructor
        public static class Category {
            private String id;
        }

        @Data
        @NoArgsConstructor
        public static class Validation {
            private List<Error> errors;
            private List<Object> warnings;

            @Data
            @NoArgsConstructor
            public static class Error {
                private String code;
                private String message;
                private String details;
                private String path;
                private String userMessage;
            }
        }

        @Data
        @NoArgsConstructor
        public static class Publication {
            private String status;
            private String duration;
            private Object endedBy;
            private Object endingAt;
            private Object startingAt;
            private boolean republish;
            private Marketplaces marketplaces;

            @Data
            @NoArgsConstructor
            public static class Marketplaces {
                private BaseMarketplace base;
                private List<Object> additional;

                @Data
                @NoArgsConstructor
                public static class BaseMarketplace {
                    private String id;
                }
            }
        }
    }
}
