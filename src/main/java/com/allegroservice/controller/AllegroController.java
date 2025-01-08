package com.allegroservice.controller;

import com.allegroservice.dto.allegro.OffersResponse;
import com.allegroservice.service.AllegroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
