package com.web.java.currency.convertor.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.java.currency.convertor.models.*;
import com.web.java.currency.convertor.models.Error;
import com.web.java.currency.convertor.utils.Constants;
import com.web.java.currency.convertor.utils.exceptions.AccessRestrictedException;
import com.web.java.currency.convertor.utils.exceptions.InvalidSourceCurrencyException;
import com.web.java.currency.convertor.utils.exceptions.InvalidTargetCurrencyException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = {
                InvalidSourceCurrencyException.class,
                ExchangeRateResponse.class,
                InvalidTargetCurrencyException.class,
                AccessRestrictedException.class
        }
)
@RestClientTest
public class RestTemplateResponseErrorHandlerIntegrationTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RestTemplateBuilder builder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test(expected = InvalidSourceCurrencyException.class)
    public void givenExchangeCurrencyApiCall_whenInvalidSource_thenThrowInvalidSourceException()
            throws JsonProcessingException {

        Assert.assertNotNull(this.builder);
        Assert.assertNotNull(this.server);

        RestTemplate restTemplate = this.builder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();

        this.server
                .expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo("/test"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.BAD_REQUEST)
                        .body(objectMapper.
                                writeValueAsString(
                                        new ExchangeRateErrorResponse(
                                                new Error(
                                                        Constants.INVALID_BASE_CURRENCY,
                                                        "msg"
                                                )
                                        )
                                )
                        )
                );

        ExchangeRateResponse response = restTemplate
                .getForObject("/test", ExchangeRateResponse.class);
        this.server.verify();
    }

    @Test(expected = InvalidTargetCurrencyException.class)
    public void givenExchangeCurrencyApiCall_whenInvaliTarget_thenThrowInvalidTargetException()
            throws JsonProcessingException {

        Assert.assertNotNull(this.builder);
        Assert.assertNotNull(this.server);

        RestTemplate restTemplate = this.builder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();

        this.server
                .expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo("/test"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.BAD_REQUEST)
                        .body(objectMapper.
                                writeValueAsString(
                                        new ExchangeRateErrorResponse(
                                                new Error(
                                                        Constants.INVALID_CURRENCY_CODES,
                                                        "msg"
                                                )
                                        )
                                )
                        )
                );

        ExchangeRateResponse response = restTemplate
                .getForObject("/test", ExchangeRateResponse.class);
        this.server.verify();
    }

    @Test(expected = AccessRestrictedException.class)
    public void givenExchangeCurrencyApiCall_whenAccessRestricted_thenThrowAccessRestrictedException()
            throws JsonProcessingException {

        Assert.assertNotNull(this.builder);
        Assert.assertNotNull(this.server);

        RestTemplate restTemplate = this.builder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();

        this.server
                .expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo("/test"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.BAD_REQUEST)
                        .body(objectMapper.
                                writeValueAsString(
                                        new ExchangeRateErrorResponse(
                                                new Error(
                                                        Constants.ACCESS_RESTRICTED,
                                                        "msg"
                                                )
                                        )
                                )
                        )
                );

        ExchangeRateResponse response = restTemplate
                .getForObject("/test", ExchangeRateResponse.class);
        this.server.verify();
    }
}
