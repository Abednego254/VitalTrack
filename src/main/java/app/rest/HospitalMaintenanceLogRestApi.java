package app.rest;

import app.ejb.MaintenanceLogEJB;
import app.model.MaintenanceLog;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;
import java.util.List;

@Path("/maintenance")
public class HospitalMaintenanceLogRestApi extends GenericApi<MaintenanceLog> {

    @EJB
    private MaintenanceLogEJB maintenanceEJB;

    @Override
    protected void saveEntity(MaintenanceLog log) throws Exception {
        maintenanceEJB.save(log);
    }

    @Override
    protected MaintenanceLog findEntity(Long id) throws Exception {
        return maintenanceEJB.findById(id);
    }

    @Override
    protected List<MaintenanceLog> listEntities() throws Exception {
        return maintenanceEJB.findAll();
    }

    @Override
    protected void deleteEntity(Long id) throws Exception {
        maintenanceEJB.delete(id);
    }
}
