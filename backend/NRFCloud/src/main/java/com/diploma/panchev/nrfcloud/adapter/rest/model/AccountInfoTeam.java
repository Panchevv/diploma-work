package com.diploma.panchev.nrfcloud.adapter.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountInfoTeam {
    @JsonProperty("tenantId")
    public String tenantId;
}
