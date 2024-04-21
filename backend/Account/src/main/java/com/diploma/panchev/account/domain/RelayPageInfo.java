package com.diploma.panchev.account.domain;

import lombok.Data;

@Data
public class RelayPageInfo {
    private boolean hasNextPage = false;
    private boolean hasPreviousPage = false;

    private long count;
    private long totalElements;
}
