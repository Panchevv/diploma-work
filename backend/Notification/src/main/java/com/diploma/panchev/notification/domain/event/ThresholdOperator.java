package com.diploma.panchev.notification.domain.event;

import lombok.Getter;

@Getter
public enum ThresholdOperator {
    EQUAL(1),
    LESS_THAN(2),
    GREATER_THAN(3);

    private final int databaseId;

    ThresholdOperator(int dbId) {
        this.databaseId = dbId;
    }

    public static ThresholdOperator fromDatabase(int id) {
        for (ThresholdOperator state : ThresholdOperator.values()) {
            if (state.getDatabaseId() == id) {
                return state;
            }
        }
        return null;
    }
}
