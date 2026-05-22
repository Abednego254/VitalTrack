package app.rest;

import app.ejb.HospitalNurseEJB;
import app.model.HospitalNurse;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;
import java.util.List;

@Path("/nurse")
public class HospitalNurseRestApi extends GenericApi<HospitalNurse> {

    @EJB
    private HospitalNurseEJB nurseEJB;

    @Override
    protected void saveEntity(HospitalNurse nurse) throws Exception {
        nurseEJB.save(nurse);
    }

    @Override
    protected HospitalNurse findEntity(Long id) throws Exception {
        return nurseEJB.findById(id);
    }

    @Override
    protected List<HospitalNurse> listEntities() throws Exception {
        return nurseEJB.findAll();
    }

    @Override
    protected void deleteEntity(Long id) throws Exception {
        nurseEJB.delete(id);
    }
}
