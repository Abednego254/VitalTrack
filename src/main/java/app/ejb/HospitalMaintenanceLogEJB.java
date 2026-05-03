package app.ejb;

import app.dao.GenericDao;
import app.model.HospitalMaintenanceLog;
import app.utility.DataSourceHelper;
import app.utility.validation.Validate;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

/**
 * [CONCEPT: @Stateless EJB]
 * The dedicated Database Specialist for Maintenance Logs.
 * WildFly manages transactions automatically for every method here.
 */
@Stateless
public class HospitalMaintenanceLogEJB {

    @Inject
    private DataSourceHelper dataSourceHelper;
    
    @Inject
    @Named("ValidMaintenanceLog")
    private Validate<HospitalMaintenanceLog> validator;
    
    private GenericDao<HospitalMaintenanceLog, Long> logDao;

    @PostConstruct
    public void init() {
        this.logDao = new GenericDao<>(HospitalMaintenanceLog.class, dataSourceHelper);
    }

    public void save(HospitalMaintenanceLog log) throws Exception {
        validator.printValidation();
        if (validator.process(log)) {
            logDao.save(log);
        } else {
            System.out.println("Bouncer says: 'Sorry, this maintenance log has bad data! Cannot save.'");
            throw new IllegalArgumentException("Maintenance log data is invalid!");
        }
    }

    public List<HospitalMaintenanceLog> findAll() throws Exception {
        return logDao.findAll();
    }
}
