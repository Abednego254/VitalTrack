package app.ejb;

import app.dao.HospitalNurseDao;
import app.model.AuditTrail;
import app.model.HospitalNurse;
import app.model.NurseAddedEvent;
import app.utility.validation.Validate;
import java.util.UUID;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Stateless
public class HospitalNurseEJB {

    @Inject
    @Named("ValidNurse")
    private Validate<HospitalNurse> validator;

    @Inject
    private Event<AuditTrail> auditTrailEvent;

    @Inject
    private HospitalNurseDao nurseDao;

    @Inject
    private Event<NurseAddedEvent> nurseAddedEvent;

    public void save(HospitalNurse nurse) throws Exception {
        // Generate temporary password BEFORE validation so @NotBlank on password passes
        String dummyPassword = "VT-TEMP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        nurse.setPassword(dummyPassword);

        validator.printValidation();
        if (validator.process(nurse)) {
            auditTrailEvent.fire(new AuditTrail("Created new Nurse: " + nurse.getName()));
            nurseDao.save(nurse);

            // Fire the welcome email event
            nurseAddedEvent.fire(new NurseAddedEvent(nurse, dummyPassword));
        } else {
            System.out.println("Bouncer says: 'Sorry, this nurse has bad data! Cannot save.'");
            throw new IllegalArgumentException("Nurse data is invalid!");
        }
    }

    public List<HospitalNurse> findAll() throws Exception {
        return nurseDao.findAll();
    }

    public HospitalNurse findById(Long id) throws Exception {
        return nurseDao.findById(id);
    }

    public HospitalNurse authenticate(String email, String password) {
        List<HospitalNurse> nurses = nurseDao.findAll();
        for (HospitalNurse n : nurses) {
            if (n.getEmail() != null && n.getEmail().equalsIgnoreCase(email)) {
                // FIRST LOGIN CHECK: temporary password
                if (n.getPassword() == null) {
                    return n;
                }
                if (n.getPassword().equals(password)) {
                    return n;
                }
            }
        }
        return null;
    }

    public void setPassword(Long nurseId, String password) throws Exception {
        HospitalNurse nurse = nurseDao.findById(nurseId);
        if (nurse != null) {
            nurse.setPassword(password);
            nurseDao.save(nurse);
            auditTrailEvent.fire(new AuditTrail("Nurse " + nurse.getName() + " set their security password."));
        }
    }

    public void delete(Long id) throws Exception {
        nurseDao.delete(id);
    }
}
