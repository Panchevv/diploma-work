package com.diploma.panchev.nrfcloud.domain;

import lombok.Data;

@Data
public class AccountDevice {
    private String clientId;
    private String caCert;
    private String clientCert;
    private String privateKey;
}
