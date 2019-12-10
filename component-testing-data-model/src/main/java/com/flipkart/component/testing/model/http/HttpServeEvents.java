package com.flipkart.component.testing.model.http;

import lombok.Getter;

import java.util.UUID;

public class HttpServeEvents {

    @Getter
    UUID id;
    @Getter
    Object request;
    @Getter
    Object stubMapping;
    @Getter
    Object responseDefinition;
    @Getter
    Object response;
    @Getter
    Object timing;

    public HttpServeEvents(UUID id, Object request, Object stubMapping, Object responseDefinition,Object response, Object timing) {
        this.id = id;
        this.request = request;
        this.stubMapping = stubMapping;
        this.responseDefinition = responseDefinition;
        this.response = response;
        this.timing = timing;
    }

    public HttpServeEvents getServeEvents() {
        return this;
    }
}
