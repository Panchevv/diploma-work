package com.diploma.panchev.apigraphql.controller.mapper;

import com.diploma.panchev.apigraphql.domain.*;
import com.diploma.panchev.apigraphql.domain.graphql.query.Connection;
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

    com.diploma.panchev.apigraphql.DeviceConnection mapDevices(Connection<Device> history);

    default com.diploma.panchev.apigraphql.DeviceEdge mapDeviceEdge(Device device) {
        com.diploma.panchev.apigraphql.DeviceEdge edge = new com.diploma.panchev.apigraphql.DeviceEdge();
        edge.setCursor(device.getId());
        edge.setNode(map(device));
        return edge;
    }

    com.diploma.panchev.apigraphql.Notification map(Notification notification);

    MeasurementType map(com.diploma.panchev.apigraphql.MeasurementType measurementType);

    com.diploma.panchev.apigraphql.DeviceMeasurement mapDeviceMeasurement(String id, Measurement measurement);

    com.diploma.panchev.apigraphql.MeasurementConnection mapMeasurements(Connection<Measurement> history);

    @Mapping(target = "node.id", source = "id")
    @Mapping(target = "node.deviceId", source = "deviceId")
    @Mapping(target = "node.type", source = "type")
    @Mapping(target = "node.value", source = "value")
    @Mapping(target = "node.when", source = "when")
    com.diploma.panchev.apigraphql.MeasurementEdge mapEdge(Measurement measurement);
}
