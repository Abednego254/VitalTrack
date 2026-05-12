package app.dao;

import app.model.HospitalMedicalSupply;
import app.utility.DataSourceHelper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class HospitalMedicalSupplyDao extends GenericDao<HospitalMedicalSupply, Long> {

    @Inject
    public HospitalMedicalSupplyDao(DataSourceHelper ds) {
        super(HospitalMedicalSupply.class);
        setDs(ds);
    }
}
