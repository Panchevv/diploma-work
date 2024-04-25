package com.diploma.panchev.nrfcloud.adapter.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountInfo {
    @JsonProperty("mqttEndpoint")
    public String mqttEndpoint;

    @JsonProperty("mqttTopicPrefix")
    public String mqttTopicPrefix;

    @JsonProperty("team")
    public AccountInfoTeam team;
}
