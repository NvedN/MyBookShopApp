package com.example.MyBookShopApp.data.google.api.books;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PanelizationSummary {
    @JsonProperty("containsEpubBubbles")
    public boolean getContainsEpubBubbles() {
        return this.containsEpubBubbles;
    }

    public void setContainsEpubBubbles(boolean containsEpubBubbles) {
        this.containsEpubBubbles = containsEpubBubbles;
    }

    boolean containsEpubBubbles;

    @JsonProperty("containsImageBubbles")
    public boolean getContainsImageBubbles() {
        return this.containsImageBubbles;
    }

    public void setContainsImageBubbles(boolean containsImageBubbles) {
        this.containsImageBubbles = containsImageBubbles;
    }

    boolean containsImageBubbles;
}
