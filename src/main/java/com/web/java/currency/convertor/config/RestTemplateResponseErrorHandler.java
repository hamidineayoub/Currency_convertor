package com.web.java.currency.convertor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.java.currency.convertor.utils.exceptions.AccessRestrictedException;
import com.web.java.currency.convertor.models.ExchangeRateErrorResponse;
import com.web.java.currency.convertor.utils.exceptions.InvalidSourceCurrencyException;
import com.web.java.currency.convertor.utils.exceptions.InvalidTargetCurrencyException;
import com.web.java.currency.convertor.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {



    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

        return (
                httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {

        if (httpResponse.getStatusCode()
                .series() == HttpStatus.Series.SERVER_ERROR) {
            throw new IOException();
        } else if (httpResponse.getStatusCode()
                .series() == HttpStatus.Series.CLIENT_ERROR) {
            if (httpResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
                ObjectMapper mapper = new ObjectMapper();
                String code = mapper.readValue(httpResponse.getBody(),
                        ExchangeRateErrorResponse.class).getError().getCode();
                switch (code) {
                    case Constants.INVALID_BASE_CURRENCY:
                        throw new InvalidSourceCurrencyException(code);
                    case Constants.INVALID_CURRENCY_CODES:
                        throw new InvalidTargetCurrencyException(code);
                    case Constants.ACCESS_RESTRICTED:
                        throw new AccessRestrictedException(code);
                    default:
                        throw new RestClientException(code);
                }
            }
        }
    }
}
