package com.diploma.panchev.apigraphql.domain.graphql.query;

import com.diploma.panchev.apigraphql.DeviceGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceGroupInternal extends DeviceGroup {
    private String accountId;
}
