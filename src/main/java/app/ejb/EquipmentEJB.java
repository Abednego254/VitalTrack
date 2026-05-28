package app.ejb;

import app.dao.EquipmentDao;
import app.model.AuditTrail;
import app.model.Equipment;
import app.utility.validation.Validate;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Stateless
public class EquipmentEJB {
    
    @Inject
    @Named("ValidEquipment")
    private Validate<Equipment> validator;

    @Inject
    private Event<AuditTrail> auditTrailEvent;

    @Inject
    private EquipmentDao equipmentDao;

    public void save(Equipment equipment) throws Exception {

        validator.printValidation();
        if (validator.process(equipment)) {
            auditTrailEvent.fire(new AuditTrail("Created new Hospital Equipment: " + equipment.getName()));
            equipmentDao.save(equipment);
        } else {
            System.out.println("Bouncer says: 'Sorry, this equipment has bad data! Cannot save.'");
            throw new IllegalArgumentException("Equipment data is invalid!");
        }
    }

    public List<Equipment> findAll() throws Exception {
        return equipmentDao.findAll();
    }

    public Equipment findById(Long id) throws Exception {
        return equipmentDao.findById(id);
    }

    public void delete(Long id) throws Exception {
        equipmentDao.delete(id);
    }
}