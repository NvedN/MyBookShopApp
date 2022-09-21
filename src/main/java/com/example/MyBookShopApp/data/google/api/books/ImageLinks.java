package com.example.MyBookShopApp.data.google.api.books;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageLinks {
    @JsonProperty("smallThumbnail")
    public String getSmallThumbnail() {
        return this.smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    String smallThumbnail;

    @JsonProperty("thumbnail")
    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    String thumbnail;
}
