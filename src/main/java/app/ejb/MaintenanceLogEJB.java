package app.ejb;

import app.dao.EquipmentDao;
import app.dao.MaintenanceLogDao;
import app.model.Equipment;
import app.model.MaintenanceLog;
import app.utility.validation.Validate;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Stateless
public class MaintenanceLogEJB {

    @Inject
    @Named("ValidMaintenanceLog")
    private Validate<MaintenanceLog> validator;
    
    @Inject
    private jakarta.enterprise.event.Event<app.model.AuditTrail> auditTrailEvent;

    @Inject
    private MaintenanceLogDao logDao;

    @Inject
    private EquipmentDao equipmentDao;

    @Inject
    @app.utility.MaintenanceQualifier(app.utility.MaintenanceChoice.STANDARD)
    private app.utility.MaintenanceService standardMaintenance;

    @Inject
    @app.utility.MaintenanceQualifier(app.utility.MaintenanceChoice.URGENT)
    private app.utility.MaintenanceService urgentMaintenance;

    public void save(MaintenanceLog log) throws Exception {
        validator.printValidation();
        if (validator.process(log)) {
            // Retrieve equipment and update calibration dates
            Equipment equipment = equipmentDao.findById(log.getEquipmentId());
            if (equipment != null) {
                java.util.Date serviceDate = log.getServiceDate();
                
                // Determine whether standard or urgent based on prior calibration gap
                boolean isUrgent = false;
                if (equipment.getNextCalibrationDate() != null && equipment.getLastCalibrationDate() != null) {
                    long diff = equipment.getNextCalibrationDate().getTime() - equipment.getLastCalibrationDate().getTime();
                    long days = diff / (1000L * 60 * 60 * 24);
                    if (days > 0 && days <= 95) {
                        isUrgent = true;
                    }
                }

                equipment.setLastCalibrationDate(serviceDate);
                java.util.Date nextCal;
                if (isUrgent) {
                    nextCal = urgentMaintenance.calculateNextMaintenanceDate(serviceDate);
                } else {
                    nextCal = standardMaintenance.calculateNextMaintenanceDate(serviceDate);
                }
                equipment.setNextCalibrationDate(nextCal);
                equipmentDao.save(equipment);
            }

            auditTrailEvent.fire(new app.model.AuditTrail("Created new Maintenance Log for Equipment ID: " + log.getEquipmentId()));
            logDao.save(log);
        } else {
            System.out.println("Bouncer says: 'Sorry, this maintenance log has bad data! Cannot save.'");
            throw new IllegalArgumentException("Maintenance log data is invalid!");
        }
    }

    public List<MaintenanceLog> findAll() throws Exception {
        List<MaintenanceLog> logs = logDao.findAll();
        for (MaintenanceLog log : logs) {
            if (log.getEquipment() != null) {
                log.setEquipmentName(log.getEquipment().getName());
            } else {
                log.setEquipmentName("Unknown Equipment (" + log.getEquipmentId() + ")");
            }
            if (log.getTechnician() != null) {
                log.setTechnicianName(log.getTechnician().getName());
            } else {
                log.setTechnicianName("System / Unknown");
            }
        }
        return logs;
    }

    public void delete(Long id) throws Exception {
        logDao.delete(id);
    }

    public MaintenanceLog findById(Long id) throws Exception {
        return logDao.findById(id);
    }
}
