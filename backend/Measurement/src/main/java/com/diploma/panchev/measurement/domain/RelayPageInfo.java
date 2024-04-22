package com.diploma.panchev.measurement.domain;

import lombok.Data;

@Data
public class RelayPageInfo {
    private boolean hasNextPage = false;
    private boolean hasPreviousPage = false;

    private long count;
    private long totalElements;
}
