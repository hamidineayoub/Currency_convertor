package com.web.java.currency.convertor.models;

public class ExchangeRateErrorResponse {

    private Error error;

    public ExchangeRateErrorResponse() {
    }

    public ExchangeRateErrorResponse(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
