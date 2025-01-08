package com.allegroservice.controller;

import com.allegroservice.dto.allegro.OffersResponse;
import com.allegroservice.service.OffersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/allegro")
public class OffersController {

    private final OffersService offersService;

    public OffersController(OffersService offersService) {
        this.offersService = offersService;
    }

    @GetMapping("/offers")
    public ResponseEntity<OffersResponse> getOffers(@RequestHeader("Authorization") String bearerToken) {
        OffersResponse offersResponse = offersService.fetchOffers(bearerToken);
        return ResponseEntity.ok(offersResponse);
    }
}
