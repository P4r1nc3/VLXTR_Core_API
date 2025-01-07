package com.allegroservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Offer {

    private String id;
    private String name;
    private Category category;
    private Image primaryImage;
    private SellingMode sellingMode;
    private SaleInfo saleInfo;
    private Stats stats;
    private Stock stock;
    private Publication publication;
    private AfterSalesServices afterSalesServices;
    private Delivery delivery;
    private B2B b2b;
    private Map<String, AdditionalMarketplace> additionalMarketplaces;

    @Data
    @NoArgsConstructor
    public static class Category {
        private String id;
    }

    @Data
    @NoArgsConstructor
    public static class Image {
        private String url;
    }

    @Data
    @NoArgsConstructor
    public static class SellingMode {
        private String format;
        private Price price;
        private Price minimalPrice;
        private Price startingPrice;
        private PriceAutomation priceAutomation;
    }

    @Data
    @NoArgsConstructor
    public static class Price {
        private String amount;
        private String currency;
    }

    @Data
    @NoArgsConstructor
    public static class PriceAutomation {
        // Pole do rozszerzenia (null w przykładzie JSON)
    }

    @Data
    @NoArgsConstructor
    public static class SaleInfo {
        private Price currentPrice;
        private int biddersCount;
    }

    @Data
    @NoArgsConstructor
    public static class Stats {
        private int watchersCount;
        private int visitsCount;
    }

    @Data
    @NoArgsConstructor
    public static class Stock {
        private int available;
        private int sold;
    }

    @Data
    @NoArgsConstructor
    public static class Publication {
        private String status;
        private String startingAt;
        private String startedAt;
        private String endingAt;
        private String endedAt;
        private Marketplaces marketplaces;

        @Data
        @NoArgsConstructor
        public static class Marketplaces {
            private BaseMarketplace base;
            private List<AdditionalMarketplace> additional;
        }

        @Data
        @NoArgsConstructor
        public static class BaseMarketplace {
            private String id;
        }
    }

    @Data
    @NoArgsConstructor
    public static class AfterSalesServices {
        private Object warranty;
        private Object returnPolicy;
        private Object impliedWarranty;
    }

    @Data
    @NoArgsConstructor
    public static class Delivery {
        private ShippingRates shippingRates;

        @Data
        @NoArgsConstructor
        public static class ShippingRates {
            private String id;
        }
    }

    @Data
    @NoArgsConstructor
    public static class B2B {
        private boolean buyableOnlyByBusiness;
    }

    @Data
    @NoArgsConstructor
    public static class AdditionalMarketplace {
        private PublicationState publication;

        @Data
        @NoArgsConstructor
        public static class PublicationState {
            private String state;
        }
    }
}
