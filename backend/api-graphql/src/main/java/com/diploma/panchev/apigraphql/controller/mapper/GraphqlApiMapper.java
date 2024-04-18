package com.diploma.panchev.apigraphql.controller.mapper;

import com.diploma.panchev.apigraphql.domain.*;
import com.diploma.panchev.apigraphql.domain.graphql.query.DeviceGroupInternal;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper( builder = @Builder( disableBuilder = true ) )
public interface GraphqlApiMapper {
    com.diploma.panchev.apigraphql.Account map(Account account);

    com.diploma.panchev.apigraphql.NrfAccountSettings map(NrfAccountSettings nrfAccountSettings);

    ThresholdNeedle map(com.diploma.panchev.apigraphql.ThresholdRequest request);

    com.diploma.panchev.apigraphql.Threshold map(Threshold threshold);

    DeviceGroupInternal map(DeviceGroup account);
}
