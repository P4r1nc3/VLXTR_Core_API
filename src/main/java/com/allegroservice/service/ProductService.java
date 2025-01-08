package com.allegroservice.service;

import com.allegroservice.dto.allegro.OffersResponse;
import com.allegroservice.model.Product;
import com.allegroservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final AllegroService allegroService;

    public ProductService(ProductRepository productRepository, AllegroService allegroService) {
        this.productRepository = productRepository;
        this.allegroService = allegroService;
    }

    public List<Product> populateProducts(String bearerToken) {
        // Fetch offers from Allegro
        OffersResponse offersResponse = allegroService.fetchOffers(bearerToken);

        List<Product> products = new ArrayList<>();
        for (OffersResponse.Offer offer : offersResponse.getOffers()) {
            // Fetch full offer details for the current offer
            OffersResponse.Offer fullOffer = allegroService.fetchOffer(bearerToken, offer.getId());

            // Extract productModel and productColour from productSet
            String productModel = null;
            String productColour = "Unknown";

            if (fullOffer.getProductSet() != null && !fullOffer.getProductSet().isEmpty()) {
                OffersResponse.Offer.ProductSet productSet = fullOffer.getProductSet().get(0);
                if (productSet.getProduct() != null && productSet.getProduct().getParameters() != null) {
                    // Extract productModel from parameters
                    productModel = productSet.getProduct().getParameters().stream()
                            .filter(param -> "Model".equalsIgnoreCase(param.getName()))
                            .findFirst()
                            .map(param -> param.getValues().get(0))
                            .orElse(null);

                    // Determine productColour from the last character of the productModel
                    if (productModel != null && productModel.contains("-")) {
                        char lastChar = productModel.charAt(productModel.length() - 1);
                        switch (lastChar) {
                            case 'W':
                                productColour = "White";
                                break;
                            case 'B':
                                productColour = "Black";
                                break;
                            case 'G':
                                productColour = "Grey";
                                break;
                        }
                    }
                }
            }

            // Ensure productModel is used for productId
            if (productModel == null) {
                productModel = "UNKNOWN_MODEL";
            }

            // Map fields to Product entity
            Product product = new Product();
            product.setProductId(productModel); // Use the productModel as the productId
            product.setProductName(fullOffer.getName());
            product.setProductColour(productColour);
            product.setProductPrice(Double.valueOf(fullOffer.getSellingMode().getPrice().getAmount()));
            product.setAllegroOfferId(fullOffer.getId());
            product.setAllegroOfferLink("https://allegro.pl/oferta/" + fullOffer.getId());
            product.setGoogleDriveId("dummyGoogleDriveId"); // Placeholder
            product.setGoogleDriveLink("https://drive.google.com/file/d/dummyGoogleDriveId/view"); // Placeholder

            // Save to database
            products.add(productRepository.save(product));
        }

        return products;
    }
}
