package app.dao;

import app.model.HospitalTechnician;
import app.utility.DataSourceHelper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class HospitalTechnicianDao extends GenericDao<HospitalTechnician, Long> {

    @Inject
    public HospitalTechnicianDao(DataSourceHelper ds) {
        super(HospitalTechnician.class);
        setDs(ds);
    }
}
