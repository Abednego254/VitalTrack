package app.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EquipmentStatusConverter implements AttributeConverter<EquipmentStatus, String> {
    @Override
    public String convertToDatabaseColumn(EquipmentStatus attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public EquipmentStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (EquipmentStatus status : EquipmentStatus.values()) {
            if (status.name().equalsIgnoreCase(dbData) || status.getLabel().equalsIgnoreCase(dbData)) {
                return status;
            }
        }
        return EquipmentStatus.ACTIVE; // Fallback for safety
    }
}
