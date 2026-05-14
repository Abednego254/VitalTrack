package app.ejb;

import app.dao.HospitalMaintenanceLogDao;
import app.model.HospitalMaintenanceLog;
import app.utility.validation.Validate;
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
    @Named("ValidMaintenanceLog")
    private Validate<HospitalMaintenanceLog> validator;
    
    @Inject
    private jakarta.enterprise.event.Event<app.model.AuditTrail> auditTrailEvent;

    @Inject
    private HospitalMaintenanceLogDao logDao;

    public void save(HospitalMaintenanceLog log) throws Exception {
        validator.printValidation();
        if (validator.process(log)) {
            auditTrailEvent.fire(new app.model.AuditTrail("Created new Maintenance Log for Equipment ID: " + log.getEquipmentId()));
            logDao.save(log);
        } else {
            System.out.println("Bouncer says: 'Sorry, this maintenance log has bad data! Cannot save.'");
            throw new IllegalArgumentException("Maintenance log data is invalid!");
        }
    }

    public List<HospitalMaintenanceLog> findAll() throws Exception {
        return logDao.findAll();
    }

    public void delete(Long id) throws Exception {
        logDao.delete(id);
    }
}
