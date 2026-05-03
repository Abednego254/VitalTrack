package app.ejb;

import app.dao.GenericDao;
import app.model.HospitalEquipment;
import app.utility.DataSourceHelper;
import app.utility.validation.Validate;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Stateless
public class HospitalEquipmentEJB {
    
    @Inject
    private DataSourceHelper dataSourceHelper;

    // [CONCEPT: CDI Specific Injection]
    // We tell CDI: "Hey, give me the specific Bouncer named 'ValidEquipment'!"
    @Inject
    @Named("ValidEquipment")
    private Validate<HospitalEquipment> validator;

    private GenericDao<HospitalEquipment, Long> equipmentDao;

    @PostConstruct
    public void init() {
        this.equipmentDao = new GenericDao<>(HospitalEquipment.class, dataSourceHelper);
    }

    public void save(HospitalEquipment hospitalEquipment) throws Exception {
        // [CONCEPT: Bouncers at the Door]
        // Before we hand the box to the Storage Worker, the Bouncer checks it!
        validator.printValidation();
        if (validator.process(hospitalEquipment)) {
            equipmentDao.save(hospitalEquipment);
        } else {
            System.out.println("Bouncer says: 'Sorry, this equipment has bad data! Cannot save.'");
            throw new IllegalArgumentException("Equipment data is invalid!");
        }
    }

    public List<HospitalEquipment> findAll() throws Exception {
        return equipmentDao.findAll();
    }
}