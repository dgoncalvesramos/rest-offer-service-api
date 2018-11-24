package com.rest.offerservice.offer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class OfferController {

    @Autowired
    private OfferServiceImpl offerServiceImpl;

    @GetMapping("/offers")
    public List<OfferEntity> retrieveAllOffers(){
        return offerServiceImpl.retrieveAllOffers();
    }

    @GetMapping("/offers/{id}")
    public Optional<OfferEntity> retrieveOffer(@PathVariable long id){
        return offerServiceImpl.retrieveOffer(id);
    }

    @PostMapping("/offers")
    public ResponseEntity<Object> createOffer(@RequestBody OfferEntity offerEntity) {
        return offerServiceImpl.createOffer(offerEntity);
    }

    @PatchMapping("/offers/{id}")
    public ResponseEntity<Object> updateOffer(@RequestBody OfferStatus offerStatus, @PathVariable long id){
        return offerServiceImpl.cancelOffer(offerStatus,id);
    }
}
