package app.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TechnicianStatusConverter implements AttributeConverter<TechnicianStatus, String> {
    @Override
    public String convertToDatabaseColumn(TechnicianStatus attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public TechnicianStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (TechnicianStatus status : TechnicianStatus.values()) {
            if (status.name().equalsIgnoreCase(dbData) || status.getLabel().equalsIgnoreCase(dbData)) {
                return status;
            }
        }
        return TechnicianStatus.ACTIVE; // Fallback for safety
    }
}
