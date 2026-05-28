package app.dao;

import app.model.Equipment;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EquipmentDao extends GenericDao<Equipment, Long> {
}
