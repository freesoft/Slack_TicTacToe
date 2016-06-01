package com.github.freesoft.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author  wonhee jung
 */
public class EphemeralMessage implements Message {

    private final String resonseType = "ephemeral";
    private String text;

    public EphemeralMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    @JsonProperty(value="response_type")
    public String getResponseType() {
        return resonseType;
    }

    public void setText(String text) {
        this.text = text;
    }
}
