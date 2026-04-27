package chatzidandis.converter;

import chatzidandis.enums.Specifier;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SpecifierConverter implements AttributeConverter<Specifier, String> {

    @Override
    public String convertToDatabaseColumn(Specifier attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public Specifier convertToEntityAttribute(String dbData) {
        return dbData != null ? Specifier.fromValue(dbData) : null;
    }
}
