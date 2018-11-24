package com.rest.offerservice.offer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface OfferService {

    List<OfferEntity> retrieveAllOffers();
    Optional<OfferEntity> retrieveOffer(long id);
    ResponseEntity<Object> createOffer(OfferEntity offerEntity);
    ResponseEntity<Object> cancelOffer(OfferStatus offerStatus, long id);
}
