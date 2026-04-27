package chatzidandis.converter;

import chatzidandis.enums.Sport;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SportConverter implements AttributeConverter<Sport, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Sport attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public Sport convertToEntityAttribute(Integer dbData) {
        return dbData != null ? Sport.fromValue(dbData) : null;
    }
}
