package app.dao;

import app.model.HospitalMaintenanceLog;
import app.utility.DataSourceHelper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class HospitalMaintenanceLogDao extends GenericDao<HospitalMaintenanceLog, Long> {

    @Inject
    public HospitalMaintenanceLogDao(DataSourceHelper ds) {
        super(HospitalMaintenanceLog.class);
        setDs(ds);
    }
}
