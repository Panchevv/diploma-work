package com.diploma.panchev.nrfcloud.adapter.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountDevice {
    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("caCert")
    private String caCert;

    @JsonProperty("clientCert")
    private String clientCert;

    @JsonProperty("privateKey")
    private String privateKey;
}
