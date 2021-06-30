package com.web.java.currency.convertor.services;

import com.web.java.currency.convertor.models.*;
import com.web.java.currency.convertor.services.impl.CurrencyServiceImpl;
import com.web.java.currency.convertor.utils.exceptions.AccessRestrictedException;
import com.web.java.currency.convertor.utils.exceptions.InvalidSourceCurrencyException;
import com.web.java.currency.convertor.utils.exceptions.InvalidTargetCurrencyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @InjectMocks
    private final CurrencyService currencyService = new CurrencyServiceImpl();

    @Mock
    private RestTemplate restTemplate;

    private final String url = "url";

    private final String key = "key";

    @BeforeEach
    private void setUp() {
        ReflectionTestUtils.setField(currencyService, "url", url);
        ReflectionTestUtils.setField(currencyService, "key", key);
    }

    @Test
    public void invalidSourceCurrency() {
        CurrencyRateRequest request = new CurrencyRateRequest("invalid", "valid", 10);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        String urlWithParams = url + "?access_key=" + key + "&base=" + request.getSource() + "&symbols=" + request.getTarget();

        HttpEntity<?> entity = new HttpEntity<>(headers);
        Mockito.when(
                restTemplate.exchange(urlWithParams, HttpMethod.GET, entity, ExchangeRateResponse.class)
        ).thenThrow(new InvalidSourceCurrencyException()
        );

        ResponseEntity response = currencyService.calculate(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(((ErrorResponse) response.getBody()).getMessage(), "Source Currency '" + request.getSource() + "' is invalid");
    }

    @Test
    public void invalidTargetCurrency() {
        CurrencyRateRequest request = new CurrencyRateRequest("invalid", "valid", 10);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        String urlWithParams = url + "?access_key=" + key + "&base=" + request.getSource() + "&symbols=" + request.getTarget();

        HttpEntity<?> entity = new HttpEntity<>(headers);
        Mockito.when(
                restTemplate.exchange(urlWithParams, HttpMethod.GET, entity, ExchangeRateResponse.class)
        ).thenThrow(new InvalidTargetCurrencyException());

        ResponseEntity response = currencyService.calculate(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(((ErrorResponse) response.getBody()).getMessage(), "Target Currency '" + request.getTarget() + "' is invalid");
    }

    @Test
    public void accessRestricted() {
        CurrencyRateRequest request = new CurrencyRateRequest("restricted", "invalid", 10);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        String urlWithParams = url + "?access_key=" + key + "&base=" + request.getSource() + "&symbols=" + request.getTarget();

        HttpEntity<?> entity = new HttpEntity<>(headers);
        Mockito.when(
                restTemplate.exchange(urlWithParams, HttpMethod.GET, entity, ExchangeRateResponse.class)
        ).thenThrow(new AccessRestrictedException());

        ResponseEntity response = currencyService.calculate(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        Assertions.assertEquals(((ErrorResponse) response.getBody()).getMessage(), "Source currency '" + request.getSource() + "' has access restriction");
    }

    @Test
    public void validRequest() {
        CurrencyRateRequest request = new CurrencyRateRequest("valid", "valid", 10);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        String urlWithParams = url + "?access_key=" + key + "&base=" + request.getSource() + "&symbols=" + request.getTarget();

        HttpEntity<?> entity = new HttpEntity<>(headers);
        Map<String,Double> rates = new HashMap<>();
        rates.put(request.getSource(),1.34);
        Mockito.when(
                restTemplate.exchange(urlWithParams, HttpMethod.GET, entity, ExchangeRateResponse.class)
        ).thenReturn(new ResponseEntity<>(new ExchangeRateResponse("valid", rates), HttpStatus.OK));

        ResponseEntity response = currencyService.calculate(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
