package com.web.java.currency.convertor.services.impl;

import com.web.java.currency.convertor.models.*;
import com.web.java.currency.convertor.services.CurrencyService;
import com.web.java.currency.convertor.utils.exceptions.AccessRestrictedException;
import com.web.java.currency.convertor.utils.exceptions.InvalidSourceCurrencyException;
import com.web.java.currency.convertor.utils.exceptions.InvalidTargetCurrencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${url}")
    private String url;

    @Value("${app.key}")
    private String key;

    private ResponseEntity<ExchangeRateResponse> getExchangeRate(String base, String target) throws AccessRestrictedException, InvalidSourceCurrencyException, InvalidTargetCurrencyException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String urlWithParams = url + "?access_key=" + key + "&base=" + base + "&symbols=" + target;
        return restTemplate.exchange(urlWithParams, HttpMethod.GET, entity, ExchangeRateResponse.class);
    }

    @Override
    public ResponseEntity calculate(CurrencyRateRequest request) {
        try {
            if (request.getTarget().isBlank() || request.getSource().isBlank()) {
                return new ResponseEntity(new ErrorResponse("Target/Source are required"), HttpStatus.BAD_REQUEST);
            }
            ResponseEntity<ExchangeRateResponse> response = getExchangeRate(request.getSource(), request.getTarget());
            ExchangeRateResponse body = response.getBody();
            double rate = body.getRates().get(request.getTarget());
            return new ResponseEntity(new CurrencyRateResponse(rate * request.getValue()), response.getStatusCode());
        } catch (AccessRestrictedException e) {
            return new ResponseEntity(new ErrorResponse("Source currency '" + request.getSource() + "' has access restriction"), HttpStatus.UNAUTHORIZED);
        } catch (InvalidSourceCurrencyException e) {
            return new ResponseEntity(new ErrorResponse("Source Currency '" + request.getSource() + "' is invalid"), HttpStatus.BAD_REQUEST);
        } catch (InvalidTargetCurrencyException e) {
            return new ResponseEntity(new ErrorResponse("Target Currency '" + request.getTarget() + "' is invalid"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
