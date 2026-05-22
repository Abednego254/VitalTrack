package app.ejb;

import app.dao.HospitalEquipmentDao;
import app.model.AuditTrail;
import app.model.HospitalEquipment;
import app.utility.validation.Validate;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Stateless
public class HospitalEquipmentEJB {
    
    @Inject
    @Named("ValidEquipment")
    private Validate<HospitalEquipment> validator;

    @Inject
    private Event<AuditTrail> auditTrailEvent;

    @Inject
    private HospitalEquipmentDao equipmentDao;

    public void save(HospitalEquipment hospitalEquipment) throws Exception {

        validator.printValidation();
        if (validator.process(hospitalEquipment)) {
            auditTrailEvent.fire(new AuditTrail("Created new Hospital Equipment: " + hospitalEquipment.getName()));
            equipmentDao.save(hospitalEquipment);
        } else {
            System.out.println("Bouncer says: 'Sorry, this equipment has bad data! Cannot save.'");
            throw new IllegalArgumentException("Equipment data is invalid!");
        }
    }

    public List<HospitalEquipment> findAll() throws Exception {
        return equipmentDao.findAll();
    }

    public HospitalEquipment findById(Long id) throws Exception {
        return equipmentDao.findById(id);
    }

    public void delete(Long id) throws Exception {
        equipmentDao.delete(id);
    }
}