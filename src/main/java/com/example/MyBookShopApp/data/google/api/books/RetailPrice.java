package com.example.MyBookShopApp.data.google.api.books;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RetailPrice {
    @JsonProperty("amount")
    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    double amount;

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    String currencyCode;

    @JsonProperty("amountInMicros")
    public int getAmountInMicros() {
        return this.amountInMicros;
    }

    public void setAmountInMicros(int amountInMicros) {
        this.amountInMicros = amountInMicros;
    }

    int amountInMicros;
}
