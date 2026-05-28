package app.ejb;

import app.dao.MedicalSupplyDao;
import app.model.AuditTrail;
import app.model.HospitalMedicalSupply;
import app.model.MedicalSupplyConsumedEvent;
import app.utility.validation.Validate;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;


@Stateless
public class MedicalSupplyEJB {

    @Inject
    @Named("ValidMedicalSupply")
    private Validate<HospitalMedicalSupply> validateMedicalSupply;

    @Inject
    private Event<AuditTrail> auditTrailEvent;

    @Inject
    private Event<MedicalSupplyConsumedEvent> consumptionEvent;

    @Inject
    private MedicalSupplyDao supplyDao;

    public void save(HospitalMedicalSupply supply) throws Exception {
        validateMedicalSupply.printValidation();
        if (validateMedicalSupply.process(supply)) {
            auditTrailEvent.fire(new AuditTrail("Created new Medical Supply: " + supply.getName()));
            supplyDao.save(supply);
        } else {
            System.out.println("Bouncer says: 'Sorry, this supply has bad data! Cannot save.'");
            throw new IllegalArgumentException("Equipment data is invalid!");
        }
    }

    public void consume(Long id, String name, int quantity) {
        consumptionEvent.fire(new MedicalSupplyConsumedEvent(id, name, quantity));
    }

    public List<HospitalMedicalSupply> findAll() throws Exception {
        return supplyDao.findAll();
    }

    public HospitalMedicalSupply findById(Long id) throws Exception {
        return supplyDao.findById(id);
    }

    public void update(HospitalMedicalSupply supply) throws Exception {
        supplyDao.save(supply);
    }

    public void delete(Long id) throws Exception {
        supplyDao.delete(id);
    }
}
