package app.rest;

import app.ejb.EquipmentEJB;
import app.model.Equipment;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;
import java.util.List;

@Path("/equipment")
public class HospitalEquipmentRestApi extends GenericApi<Equipment> {

    @EJB
    private EquipmentEJB equipmentEJB;

    @Override
    protected void saveEntity(Equipment equipment) throws Exception {
        equipmentEJB.save(equipment);
    }

    @Override
    protected Equipment findEntity(Long id) throws Exception {
        return equipmentEJB.findById(id);
    }

    @Override
    protected List<Equipment> listEntities() throws Exception {
        return equipmentEJB.findAll();
    }

    @Override
    protected void deleteEntity(Long id) throws Exception {
        equipmentEJB.delete(id);
    }
}
