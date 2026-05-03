package app.ejb;

import app.dao.GenericDao;
import app.model.HospitalTechnician;
import app.utility.DataSourceHelper;
import app.utility.validation.Validate;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
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
    private DataSourceHelper dataSourceHelper;
    
    @Inject
    @Named("ValidTechnician")
    private Validate<HospitalTechnician> validator;
    
    private GenericDao<HospitalTechnician, Long> technicianDao;

    @PostConstruct
    public void init() {
        this.technicianDao = new GenericDao<>(HospitalTechnician.class, dataSourceHelper);
    }

    public void save(HospitalTechnician technician) throws Exception {
        validator.printValidation();
        if (validator.process(technician)) {
            technicianDao.save(technician);
        } else {
            System.out.println("Bouncer says: 'Sorry, this technician has bad data! Cannot save.'");
            throw new IllegalArgumentException("Technician data is invalid!");
        }
    }

    public List<HospitalTechnician> findAll() throws Exception {
        return technicianDao.findAll();
    }
}
