package com.diploma.panchev.measurement.domain;

import lombok.Data;

import java.util.List;

@Data
public class Relay<T> {
    private RelayPageInfo pageInfo;
    private List<T> items;
}
