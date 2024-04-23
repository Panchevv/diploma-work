package com.diploma.panchev.notification.utils.convertor;

import com.diploma.panchev.notification.domain.event.MeasurementType;
import jakarta.persistence.AttributeConverter;

public class MeasurementTypeConverter implements AttributeConverter<MeasurementType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MeasurementType attribute) {
        return attribute == null ? null : attribute.getDatabaseId();
    }

    @Override
    public MeasurementType convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : MeasurementType.fromDatabase(dbData);
    }
}
