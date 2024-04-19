package com.diploma.panchev.apigraphql.domain.graphql.query;

import lombok.Data;

@Data
public class PageInfo {
    private Boolean hasNextPage = false;
    private Boolean hasPreviousPage = false;
    private String startCursor = "";
    private String endCursor = "";
    private Long count;
    private Long totalElements;
}
