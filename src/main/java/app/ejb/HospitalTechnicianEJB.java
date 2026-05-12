package app.ejb;

import app.dao.HospitalTechnicianDao;
import app.model.AuditTrail;
import app.model.HospitalTechnician;
import app.model.TechnicianAddedEvent;
import app.utility.validation.Validate;
import java.util.UUID;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

/**
 * [CONCEPT: @Stateless EJB]
 * The dedicated Database Specialist for Technicians.
 * WildFly manages transactions automatically for every method here.
 */
@Stateless
public class HospitalTechnicianEJB {

    @Inject
    @Named("ValidTechnician")
    private Validate<HospitalTechnician> validator;
    
    @Inject
    private Event<AuditTrail> auditTrailEvent;

    @Inject
    private HospitalTechnicianDao technicianDao;

    @Inject
    private Event<TechnicianAddedEvent> technicianAddedEvent;

    public void save(HospitalTechnician technician) throws Exception {
        validator.printValidation();
        if (validator.process(technician)) {
            // Generate a temporary dummy password
            String dummyPassword = "VT-TEMP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            technician.setPassword(dummyPassword);

            auditTrailEvent.fire(new AuditTrail("Created new Technician: " + technician.getName()));
            technicianDao.save(technician);
            
            // Fire the email event
            technicianAddedEvent.fire(new TechnicianAddedEvent(technician, dummyPassword));
        } else {
            System.out.println("Bouncer says: 'Sorry, this technician has bad data! Cannot save.'");
            throw new IllegalArgumentException("Technician data is invalid!");
        }
    }

    public List<HospitalTechnician> findAll() throws Exception {
        return technicianDao.findAll();
    }

    public HospitalTechnician authenticate(String email, String password) {
        List<HospitalTechnician> techs = technicianDao.findAll();
        for (HospitalTechnician t : techs) {
            // WHITELIST CHECK: Match email
            if (t.getEmail() != null && t.getEmail().equalsIgnoreCase(email)) {
                // FIRST LOGIN CHECK: If password is null, they haven't set it yet
                if (t.getPassword() == null) {
                    return t; // Let them in to set password
                }
                // REGULAR LOGIN: Check password
                if (t.getPassword().equals(password)) {
                    return t;
                }
            }
        }
        return null;
    }

    public void setPassword(Long techId, String password) throws Exception {
        HospitalTechnician tech = technicianDao.findById(techId);
        if (tech != null) {
            tech.setPassword(password);
            technicianDao.update(tech);
            auditTrailEvent.fire(new AuditTrail("Technician " + tech.getName() + " set their security password."));
        }
    }
}
