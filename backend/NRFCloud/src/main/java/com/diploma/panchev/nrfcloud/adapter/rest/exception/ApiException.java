package com.diploma.panchev.nrfcloud.adapter.rest.exception;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ApiException extends RuntimeException {
    private final int code;
    private final Map<String, List<String>> responseHeaders;
    private final String responseBody;

    public ApiException(String message, Throwable throwable) {
        this(message, throwable, 0, null, null);
    }
    public ApiException(String message, Throwable throwable, int code, Map<String, List<String>> responseHeaders, String responseBody) {
        super(message, throwable);
        this.code = code;
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
    }
}
