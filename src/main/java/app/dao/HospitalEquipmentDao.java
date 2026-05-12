package app.dao;

import app.model.HospitalEquipment;
import app.utility.DataSourceHelper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class HospitalEquipmentDao extends GenericDao<HospitalEquipment, Long> {

    @Inject
    public HospitalEquipmentDao(DataSourceHelper ds) {
        super(HospitalEquipment.class);
        setDs(ds);
    }
}
