package com.web.java.currency.convertor.utils.exceptions;

import org.springframework.web.client.RestClientException;

public class AccessRestrictedException extends RestClientException {
    public AccessRestrictedException(String msg) {
        super(msg);
    }
    public AccessRestrictedException(){
        super("Access restricted");
    }
}
