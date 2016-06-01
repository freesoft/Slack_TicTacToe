package com.github.freesoft.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wonhee jung
 */
public class InChannelMessage implements Message {

    private final String responseType = "in_channel";
    private String text;

    public InChannelMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    @JsonProperty(value="response_type")
    public String getResponseType() {
        return responseType;
    }

    public void setText(String text) {
        this.text = text;
    }
}
