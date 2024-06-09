package com.diploma.panchev.notification.domain.event;

import lombok.Getter;

@Getter
public enum MeasurementType {
    TEMPERATURE(1),
    HUMIDITY(2),
    AIR_PRESSURE(3),
    BATTERY_VOLTAGE(4),
    RSRP(8);

    private final int databaseId;

    MeasurementType(int dbId) {
        this.databaseId = dbId;
    }

    public static MeasurementType fromDatabase(int id) {
        for (MeasurementType state : MeasurementType.values()) {
            if (state.getDatabaseId() == id) {
                return state;
            }
        }
        return null;
    }
}
