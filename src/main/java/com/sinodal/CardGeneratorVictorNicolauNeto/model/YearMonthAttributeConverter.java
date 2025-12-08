package com.sinodal.CardGeneratorVictorNicolauNeto.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.YearMonth;

@Converter(autoApply = true)
public class YearMonthAttributeConverter implements AttributeConverter<YearMonth, String> {

    @Override
    public String convertToDatabaseColumn(YearMonth attribute) {
        return attribute == null ? null : attribute.toString(); // e.g. 2025-10
    }

    @Override
    public YearMonth convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isEmpty()) ? null : YearMonth.parse(dbData);
    }
}
