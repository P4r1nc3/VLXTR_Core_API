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

    public List<Product> populateProductsFromAllegro(String bearerToken) {
        // Fetch offers from Allegro using the AllegroService
        OffersResponse offersResponse = allegroService.fetchOffers(bearerToken);

        // Process each Offer and save to the database
        List<Product> products = new ArrayList<>();
        for (OffersResponse.Offer offer : offersResponse.getOffers()) {
            // Map fields from Offer to Product
            Product product = new Product();
            product.setProductId(offer.getId());
            product.setProductName(offer.getName());
            product.setProductColour("Default Colour"); // Replace with real data if available
            product.setProductPrice(Double.valueOf(offer.getSellingMode().getPrice().getAmount()));
            product.setAllegroOfferId(offer.getId());
            product.setAllegroOfferLink("https://allegro.pl/oferta/" + offer.getId());
            product.setGoogleDriveId("dummyGoogleDriveId"); // Placeholder
            product.setGoogleDriveLink("https://drive.google.com/file/d/dummyGoogleDriveId/view"); // Placeholder

            // Save to the database
            products.add(productRepository.save(product));
        }

        return products;
    }
}
