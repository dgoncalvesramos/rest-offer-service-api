package com.rest.offerservice.exceptions;

public class OfferBadFormatException extends RuntimeException {
    private Long offerId;
    private String message;

    public OfferBadFormatException(Long offerId, String message) {
        this.offerId = offerId;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "OfferBadFormatException{" +
                "offerId=" + offerId +
                '}'+ " - " + this.message;
    }
}
