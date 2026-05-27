package app.ejb;

import app.dao.HospitalMedicalSupplyDao;
import app.model.HospitalMedicalSupply;
import app.utility.validation.Validate;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;


@Stateless
public class HospitalMedicalSupplyEJB {

    @Inject
    @Named("ValidMedicalSupply")
    private Validate<HospitalMedicalSupply> validateMedicalSupply;

    @Inject
    private jakarta.enterprise.event.Event<app.model.AuditTrail> auditTrailEvent;

    @Inject
    private HospitalMedicalSupplyDao supplyDao;

    public void save(HospitalMedicalSupply supply) throws Exception {
        validateMedicalSupply.printValidation();
        if (validateMedicalSupply.process(supply)) {
            auditTrailEvent.fire(new app.model.AuditTrail("Created new Medical Supply: " + supply.getName()));
            supplyDao.save(supply);
        } else {
            System.out.println("Bouncer says: 'Sorry, this supply has bad data! Cannot save.'");
            throw new IllegalArgumentException("Equipment data is invalid!");
        }
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
