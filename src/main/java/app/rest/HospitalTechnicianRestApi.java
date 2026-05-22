package app.rest;

import app.ejb.HospitalTechnicianEJB;
import app.model.HospitalTechnician;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;
import java.util.List;

@Path("/technician")
public class HospitalTechnicianRestApi extends GenericApi<HospitalTechnician> {

    @EJB
    private HospitalTechnicianEJB technicianEJB;

    @Override
    protected void saveEntity(HospitalTechnician tech) throws Exception {
        technicianEJB.save(tech);
    }

    @Override
    protected HospitalTechnician findEntity(Long id) throws Exception {
        return technicianEJB.findById(id);
    }

    @Override
    protected List<HospitalTechnician> listEntities() throws Exception {
        return technicianEJB.findAll();
    }

    @Override
    protected void deleteEntity(Long id) throws Exception {
        technicianEJB.delete(id);
    }
}
