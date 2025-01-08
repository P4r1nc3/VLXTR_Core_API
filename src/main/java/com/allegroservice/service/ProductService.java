package com.allegroservice.service;

import com.google.api.services.drive.model.File;
import com.allegroservice.dto.allegro.OffersResponse;
import com.allegroservice.model.Product;
import com.allegroservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final AllegroService allegroService;
    private final GoogleDriveService googleDriveService;
    private final ProductRepository productRepository;


    public ProductService(AllegroService allegroService, GoogleDriveService googleDriveService, ProductRepository productRepository) {
        this.allegroService = allegroService;
        this.googleDriveService = googleDriveService;
        this.productRepository = productRepository;
    }

    public List<Product> populateProducts(String bearerToken) {
        // Fetch offers from Allegro
        OffersResponse offersResponse = allegroService.fetchOffers(bearerToken);

        // Fetch files from Google Drive
        List<File> googleFiles = googleDriveService.fetchGoogleDriveFiles();

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

            // Final copy of productModel for use in lambda expression
            final String finalProductModel = productModel;

            // Find the matching file in Google Drive
            Optional<File> matchingFile = googleFiles.stream()
                    .filter(file -> file.getName().startsWith(finalProductModel + ".") || file.getName().equals(finalProductModel))
                    .findFirst();

            String googleDriveId = matchingFile.map(File::getId).orElse("UNKNOWN_FILE_ID");
            String googleDriveLink = "https://drive.google.com/file/d/" + googleDriveId + "/view";

            // Map fields to Product entity
            Product product = new Product();
            product.setProductId(productModel); // Use the productModel as the productId
            product.setProductName(fullOffer.getName());
            product.setProductColour(productColour);
            product.setProductPrice(Double.valueOf(fullOffer.getSellingMode().getPrice().getAmount()));
            product.setAllegroOfferId(fullOffer.getId());
            product.setAllegroOfferLink("https://allegro.pl/oferta/" + fullOffer.getId());
            product.setGoogleDriveId(googleDriveId);
            product.setGoogleDriveLink(googleDriveLink);

            // Save to database
            products.add(productRepository.save(product));
        }

        return products;
    }
}
