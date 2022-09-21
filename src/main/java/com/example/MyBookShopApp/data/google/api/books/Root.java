package com.example.MyBookShopApp.data.google.api.books;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.List;

public class Root {
    @JsonProperty("kind")
    public String getKind() {
        return this.kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    String kind;

    @JsonProperty("totalItems")
    public BigInteger getTotalItems() {
        return this.totalItems;
    }

    public void setTotalItems(BigInteger  totalItems) {
        this.totalItems = totalItems;
    }

    BigInteger  totalItems;

    @JsonProperty("items")
    public List<Item> getItems() {
        return this.items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    List<Item> items;
}
