package com.diploma.panchev.apigraphql.controller.mapper;

import com.diploma.panchev.apigraphql.domain.*;
import com.diploma.panchev.apigraphql.domain.graphql.query.DeviceGroupInternal;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper( builder = @Builder( disableBuilder = true ) )
public interface GraphqlApiMapper {
    com.diploma.panchev.apigraphql.Account map(Account account);

    com.diploma.panchev.apigraphql.NrfAccountSettings map(NrfAccountSettings nrfAccountSettings);

    ThresholdNeedle map(com.diploma.panchev.apigraphql.ThresholdRequest request);

    com.diploma.panchev.apigraphql.Threshold map(Threshold threshold);

    DeviceGroupInternal map(DeviceGroup account);

    com.diploma.panchev.apigraphql.SubscriptionSession map(SubscriptionSession subscriptionSession);

    @Mapping(target = "id", source = "deviceId")
    com.diploma.panchev.apigraphql.Device map(Device device);
}
