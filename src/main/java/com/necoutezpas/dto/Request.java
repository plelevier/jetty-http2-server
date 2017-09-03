package com.necoutezpas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder(toBuilder = true)
@JsonPOJOBuilder(withPrefix = "")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request {
    private final long timestamp;
    private final String protocol;
    private final boolean isSecure;
    private final String method;
    private final String path;
    private final String query;
    private final Map<String,List<String>> headers;
    private final String body;
}
