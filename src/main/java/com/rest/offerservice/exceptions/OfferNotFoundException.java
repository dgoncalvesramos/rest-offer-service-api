package com.rest.offerservice.exceptions;

public class OfferNotFoundException extends RuntimeException {
    private Long offerId;

    public OfferNotFoundException(Long offerId) {
        this.offerId = offerId;
    }

    @Override
    public String toString() {
        return "OfferNotFoundException{" +
                "offerId=" + offerId +
                '}';
    }
}
