package app.rest;

import app.ejb.HospitalMedicalSupplyEJB;
import app.model.HospitalMedicalSupply;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;
import java.util.List;

@Path("/supply")
public class HospitalMedicalSupplyRestApi extends GenericApi<HospitalMedicalSupply> {

    @EJB
    private HospitalMedicalSupplyEJB supplyEJB;

    @Override
    protected void saveEntity(HospitalMedicalSupply supply) throws Exception {
        supplyEJB.save(supply);
    }

    @Override
    protected HospitalMedicalSupply findEntity(Long id) throws Exception {
        return supplyEJB.findById(id);
    }

    @Override
    protected List<HospitalMedicalSupply> listEntities() throws Exception {
        return supplyEJB.findAll();
    }

    @Override
    protected void deleteEntity(Long id) throws Exception {
        supplyEJB.delete(id);
    }
}
