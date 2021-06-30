package com.web.java.currency.convertor.models;

public class CurrencyRateResponse {
    private double value;

    public CurrencyRateResponse(double value) {
        this.value = value;
    }

    public CurrencyRateResponse() {
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
