package com.web.java.currency.convertor.services;

import com.web.java.currency.convertor.models.CurrencyRateRequest;
import org.springframework.http.ResponseEntity;

public interface CurrencyService {

    ResponseEntity calculate(CurrencyRateRequest request);

}
