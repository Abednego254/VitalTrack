package app.rest;

import app.ejb.HospitalEquipmentEJB;
import app.model.HospitalEquipment;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;
import java.util.List;

@Path("/equipment")
public class HospitalEquipmentRestApi extends GenericApi<HospitalEquipment> {

    @EJB
    private HospitalEquipmentEJB equipmentEJB;

    @Override
    protected void saveEntity(HospitalEquipment equipment) throws Exception {
        equipmentEJB.save(equipment);
    }

    @Override
    protected HospitalEquipment findEntity(Long id) throws Exception {
        return equipmentEJB.findById(id);
    }

    @Override
    protected List<HospitalEquipment> listEntities() throws Exception {
        return equipmentEJB.findAll();
    }

    @Override
    protected void deleteEntity(Long id) throws Exception {
        equipmentEJB.delete(id);
    }
}
