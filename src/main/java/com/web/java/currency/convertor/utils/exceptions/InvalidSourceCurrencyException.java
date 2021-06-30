package com.web.java.currency.convertor.utils.exceptions;

import org.springframework.web.client.RestClientException;


public class InvalidSourceCurrencyException extends RestClientException {

    public InvalidSourceCurrencyException(String msg) {
        super(msg);
    }

    public InvalidSourceCurrencyException(){
        super("Invalid source currency");

    }
}
