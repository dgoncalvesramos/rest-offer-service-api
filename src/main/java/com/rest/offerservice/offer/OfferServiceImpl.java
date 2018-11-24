package com.rest.offerservice.offer;

import com.rest.offerservice.exceptions.OfferBadFormatException;
import com.rest.offerservice.exceptions.OfferNotFoundException;
import com.rest.offerservice.exceptions.OfferPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Override
    public ResponseEntity<Object> createOffer(OfferEntity offerEntity) {

        if(offerEntity.getExpirationDate() == null){
            throw new OfferBadFormatException(offerEntity.getId(),"Expiration date must be provided for offer");
        }

        if(offerEntity.getStatus().equals(OfferStatus.CLOSE)){
            throw new OfferBadFormatException(offerEntity.getId(),"Status of the offer must be OPEN");
        }

        if(offerEntity.getExpirationDate().compareTo(Instant.now())<0){
            throw new OfferBadFormatException(offerEntity.getId(),"Expiration date must be after current date");
        }

        if(offerEntity.getPrice() == null || offerEntity.getPrice().equals(new BigDecimal(0))){
            throw new OfferBadFormatException(offerEntity.getId(),"Price must be provided for the offer");
        }

        OfferEntity savedOfferEntity = offerRepository.save(offerEntity);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedOfferEntity.getId()).toUri();
        return  ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Object> cancelOffer(OfferStatus offerStatus, long id) {

        if(offerStatus.equals(OfferStatus.OPEN))
            throw new OfferPatchException(id, "You can only cancel an existing offer");

        Optional<OfferEntity> offer = offerRepository.findById(id);

        if(!offer.isPresent())
            throw new OfferPatchException(id, "You can't cancel a non existing offer");

        OfferEntity fetchedOffer = offer.get();
        if(fetchedOffer.getStatus().equals(OfferStatus.CLOSE))
            throw new OfferPatchException(id, "You can't cancel an offer already closed");
        fetchedOffer.setStatus(OfferStatus.CLOSE);
        offerRepository.save(fetchedOffer);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return  ResponseEntity.noContent().location(location).build();
    }

    @Override
    public List<OfferEntity> retrieveAllOffers() {
        List<OfferEntity> entities = offerRepository.findAll();

        //Trigger status change if expiration date > current date
        for(OfferEntity entity : entities){
            verifyIfOfferIsExpiredAndUpdateStatus(entity);
        }
        return entities;
    }

    @Override
    public Optional<OfferEntity> retrieveOffer(long id) {
        Optional<OfferEntity> offer = offerRepository.findById(id);

        if (!offer.isPresent()) {
            throw new OfferNotFoundException(id);
        }

        //Trigger status change if expiration date > current date
        verifyIfOfferIsExpiredAndUpdateStatus(offer.get());

        return offer;
    }

    private void verifyIfOfferIsExpiredAndUpdateStatus(OfferEntity offerEntity) {
        if(offerEntity.getExpirationDate().compareTo(Instant.now())<0) {
            offerEntity.setStatus(OfferStatus.CLOSE);
            offerRepository.save(offerEntity);
        }
    }
}
