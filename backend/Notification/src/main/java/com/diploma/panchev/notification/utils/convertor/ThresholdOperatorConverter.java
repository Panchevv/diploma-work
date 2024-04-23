package com.diploma.panchev.notification.utils.convertor;

import com.diploma.panchev.notification.domain.event.ThresholdOperator;
import jakarta.persistence.AttributeConverter;

public class ThresholdOperatorConverter implements AttributeConverter<ThresholdOperator, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ThresholdOperator attribute) {
        return attribute == null ? null : attribute.getDatabaseId();
    }

    @Override
    public ThresholdOperator convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : ThresholdOperator.fromDatabase(dbData);
    }
}
