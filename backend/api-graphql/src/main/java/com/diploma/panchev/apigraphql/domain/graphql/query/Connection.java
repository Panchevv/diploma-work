package com.diploma.panchev.apigraphql.domain.graphql.query;

import lombok.Data;

import java.util.List;

@Data
public class Connection<T> {
    private List<T> edges;
    private PageInfo pageInfo;
}
