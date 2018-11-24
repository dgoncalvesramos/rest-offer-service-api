package com.rest.offerservice.exceptions;

public class OfferPatchException extends RuntimeException {
    private Long offerId;
    private String message;

    public OfferPatchException(Long offerId, String message) {
        this.offerId = offerId;
        this.message = message;
    }

    @Override
    public String toString() {
        return "OfferPatchException{" +
                "offerId=" + offerId +
                '}' + " - " + message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
