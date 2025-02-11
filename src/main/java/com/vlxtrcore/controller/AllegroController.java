package com.vlxtrcore.controller;

import com.vlxtrcore.dto.allegro.OffersResponse;
import com.vlxtrcore.service.AllegroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/allegro")
public class AllegroController {

    private final AllegroService allegroService;

    public AllegroController(AllegroService allegroService) {
        this.allegroService = allegroService;
    }

    @GetMapping("/offers")
    public ResponseEntity<OffersResponse> getOffers(@RequestHeader("Authorization") String bearerToken) {
        OffersResponse offersResponse = allegroService.fetchOffers(bearerToken);
        return ResponseEntity.ok(offersResponse);
    }

    @GetMapping("/offers/{offerId}")
    public ResponseEntity<OffersResponse.Offer> getOffer(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable String offerId) {
        OffersResponse.Offer offer = allegroService.fetchOffer(bearerToken, offerId);
        return ResponseEntity.ok(offer);
    }
}
