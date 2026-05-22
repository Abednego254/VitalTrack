package app.rest;

import app.ejb.HospitalMaintenanceLogEJB;
import app.model.HospitalMaintenanceLog;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;
import java.util.List;

@Path("/maintenance")
public class HospitalMaintenanceLogRestApi extends GenericApi<HospitalMaintenanceLog> {

    @EJB
    private HospitalMaintenanceLogEJB maintenanceEJB;

    @Override
    protected void saveEntity(HospitalMaintenanceLog log) throws Exception {
        maintenanceEJB.save(log);
    }

    @Override
    protected HospitalMaintenanceLog findEntity(Long id) throws Exception {
        return maintenanceEJB.findById(id);
    }

    @Override
    protected List<HospitalMaintenanceLog> listEntities() throws Exception {
        return maintenanceEJB.findAll();
    }

    @Override
    protected void deleteEntity(Long id) throws Exception {
        maintenanceEJB.delete(id);
    }
}
