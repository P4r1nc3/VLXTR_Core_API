package com.allegroservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OffersResponse {
    private List<Offer> offers;
    private int count;
    private int totalCount;
}
