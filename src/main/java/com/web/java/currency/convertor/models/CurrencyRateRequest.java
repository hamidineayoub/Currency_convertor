package com.web.java.currency.convertor.models;

public class CurrencyRateRequest {

    private String source;
    private String target;
    private double value;

    public CurrencyRateRequest() {
    }

    public CurrencyRateRequest(String source, String target, double value) {
        this.source = source;
        this.target = target;
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
