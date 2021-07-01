package com.web.java.currency.convertor.controllers;

import com.web.java.currency.convertor.models.CurrencyRateRequest;

import com.web.java.currency.convertor.services.CurrencyService;
import io.github.sercasti.tracing.Traceable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CurrencyController {

    @Autowired
    CurrencyService currencyService;


    @PostMapping("/calculate")
    @Traceable
    public ResponseEntity calculate(@RequestBody CurrencyRateRequest request) {
        return currencyService.calculate(request);
    }

}
