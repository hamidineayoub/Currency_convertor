package com.web.java.currency.convertor.utils.exceptions;

import org.springframework.web.client.RestClientException;

import java.io.IOException;

public class InvalidTargetCurrencyException extends RestClientException {
    public InvalidTargetCurrencyException(String msg) {
        super(msg);
    }
    public InvalidTargetCurrencyException(){
        super("Invalid target currency");
    }
}
