package com.vlxtrcore.service;

import com.google.api.services.drive.model.File;
import com.p4r1nc3.vlxtr.allegro.api.OffersApi;
import com.p4r1nc3.vlxtr.allegro.model.OfferDetailsResponse;
import com.p4r1nc3.vlxtr.allegro.model.OfferListItem;
import com.p4r1nc3.vlxtr.allegro.model.OfferProductSetItem;
import com.p4r1nc3.vlxtr.allegro.model.OffersListResponse;
import com.vlxtrcore.exception.ApiException;
import com.vlxtrcore.model.Product;
import com.vlxtrcore.repository.ProductRepository;
import com.vlxtrcore.service.factory.AllegroApiClientFactory;
import com.vlxtrcore.validators.ProductValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final GoogleDriveService googleDriveService;
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;
    private final AllegroApiClientFactory allegroApiClientFactory;

    public ProductService(GoogleDriveService googleDriveService,
                          ProductRepository productRepository,
                          ProductValidator productValidator,
                          AllegroApiClientFactory allegroApiClientFactory) {
        this.googleDriveService = googleDriveService;
        this.productRepository = productRepository;
        this.productValidator = productValidator;
        this.allegroApiClientFactory = allegroApiClientFactory;
    }

    public List<Product> getProducts() {
        logger.info("Fetching all products from the database.");
        return productRepository.findAll();
    }

    public Product getProductByAllegroOfferId(String allegroOfferId) {
        logger.info("Fetching product with Allegro Offer ID: {}", allegroOfferId);
        Optional<Product> product = productRepository.findByAllegroOfferId(allegroOfferId);
        return product.orElseThrow(() -> {
            logger.warn("Product not found for Allegro Offer ID: {}", allegroOfferId);
            return new ApiException(HttpStatus.NOT_FOUND,
                    "Product not found",
                    "Product with allegroOfferId: " + allegroOfferId + " not found",
                    "Verify the Allegro Offer ID and try again.");
        });
    }

    public List<Product> populateProducts() {
        logger.info("Starting product population from Allegro and Google Drive.");
        try {
            OffersApi offersApi = allegroApiClientFactory.createOffersApi();
            OffersListResponse offersListResponse = offersApi.getOffers();
            List<File> googleFiles = googleDriveService.fetchGoogleDriveFiles();
            logger.info("Fetched {} offers from Allegro API.", offersListResponse.getOffers().size());
            logger.info("Fetched {} files from Google Drive.", googleFiles.size());

            return offersListResponse.getOffers()
                    .stream()
                    .map(offer -> processOffer(offer, googleFiles))
                    .map(productRepository::save)
                    .toList();
        } catch (Exception e) {
            logger.error("Error while populating products: {}", e.getMessage(), e);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to populate products",
                    e.getMessage(),
                    "Check API connectivity and try again.");
        }
    }

    private Product processOffer(OfferListItem offer, List<File> googleFiles) {
        logger.info("Processing Allegro offer ID: {}", offer.getId());
        try {
            OffersApi offersApi = allegroApiClientFactory.createOffersApi();
            OfferDetailsResponse fullOffer = offersApi.getOfferById(offer.getId());

            String productModel = extractProductModel(fullOffer);
            String productColour = extractProductColour(productModel);
            String googleDriveId = findGoogleDriveId(googleFiles, productModel);
            String googleDriveLink = "https://drive.google.com/file/d/" + googleDriveId + "/view";

            Product product = buildProductEntity(fullOffer, productModel, productColour, googleDriveId, googleDriveLink);
            logger.info("Successfully processed offer ID: {} - Model: {} - Colour: {}",
                    offer.getId(), productModel, productColour);
            return product;
        } catch (Exception e) {
            logger.error("Failed to process offer ID: {}. Error: {}", offer.getId(), e.getMessage(), e);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to process offer",
                    e.getMessage(),
                    "Check offer details and try again.");
        }
    }

    private String extractProductModel(OfferDetailsResponse fullOffer) {
        if (fullOffer.getProductSet() != null && !fullOffer.getProductSet().isEmpty()) {
            OfferProductSetItem productSet = fullOffer.getProductSet().get(0);
            if (productSet.getProduct() != null && productSet.getProduct().getParameters() != null) {
                return productSet.getProduct().getParameters().stream()
                        .filter(param -> "Model".equalsIgnoreCase(param.getName()))
                        .findFirst()
                        .map(param -> param.getValues().get(0))
                        .orElse("UNKNOWN_MODEL");
            }
        }
        return "UNKNOWN_MODEL";
    }

    private String extractProductColour(String productModel) {
        if (productModel != null && productModel.contains("-")) {
            String[] parts = productModel.split("-");
            return parts[parts.length - 1];
        }
        return "UNKNOWN_COLOUR";
    }

    private String findGoogleDriveId(List<File> googleFiles, String productModel) {
        String googleDriveId = googleFiles.stream()
                .filter(file -> file.getName().startsWith(productModel + ".") || file.getName().equals(productModel))
                .findFirst()
                .map(File::getId)
                .orElse("UNKNOWN_FILE_ID");

        if ("UNKNOWN_FILE_ID".equals(googleDriveId)) {
            logger.warn("No matching Google Drive file found for product model: {}", productModel);
        } else {
            logger.info("Found Google Drive file for product model: {} - File ID: {}", productModel, googleDriveId);
        }
        return googleDriveId;
    }

    private Product buildProductEntity(OfferDetailsResponse fullOffer, String productModel, String productColour,
                                       String googleDriveId, String googleDriveLink) {
        Product product = new Product();
        product.setProductId(productModel);
        product.setProductName(fullOffer.getName());
        product.setProductColour(productColour);
        product.setProductPrice(Double.valueOf(fullOffer.getSellingMode().getPrice().getAmount()));
        product.setAllegroOfferId(fullOffer.getId());
        product.setAllegroOfferLink("https://allegro.pl/oferta/" + fullOffer.getId());
        product.setGoogleDriveId(googleDriveId);
        product.setGoogleDriveLink(googleDriveLink);
        product.setIsValid(productValidator.isValid(product));

        logger.info("Built product entity - ID: {}, Name: {}, Colour: {}, Price: {}",
                productModel, fullOffer.getName(), productColour, product.getProductPrice());

        return product;
    }
}
