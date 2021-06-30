package com.web.java.currency.convertor.controllers;

import com.web.java.currency.convertor.models.CurrencyRateRequest;

import com.web.java.currency.convertor.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyController {

    @Autowired
    CurrencyService currencyService;


    @PostMapping("/calculate")
    public ResponseEntity calculate(@RequestBody CurrencyRateRequest request){
        return currencyService.calculate(request);
    }
}
