package app.utility.bootstrap;

import app.model.AuditTrail;
import app.model.HospitalEquipment;
import app.model.HospitalMaintenanceLog;
import app.model.HospitalMedicalSupply;
import app.model.HospitalTechnician;
import app.model.User;
import app.utility.DataSourceHelper;
import app.utility.MaintenanceChoice;
import app.utility.MaintenanceQualifier;
import app.utility.MaintenanceService;
import app.utility.db.TableGenerator;
import app.dao.GenericDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class DatabaseBootstrap implements Bootstrap {

    @Inject
    private DataSourceHelper dataSourceHelper;

    @Inject
    @MaintenanceQualifier(MaintenanceChoice.STANDARD)
    private MaintenanceService standardMaintenance;

    @Inject
    @MaintenanceQualifier(MaintenanceChoice.URGENT)
    private MaintenanceService urgentMaintenance;

    @Override
    public void process() {
        System.out.println("Morning Checklist: Building Database Tables...");

        // We collect all our models that have the sticky notes
        Set<Class<?>> entities = new HashSet<>();
        entities.add(HospitalEquipment.class);
        entities.add(HospitalMedicalSupply.class);
        entities.add(HospitalTechnician.class);
        entities.add(HospitalMaintenanceLog.class);
        entities.add(User.class);
        entities.add(AuditTrail.class);

        // We give them to the Table Generator Builder
        try (Connection conn = dataSourceHelper.getConnection()) {
            TableGenerator.generateTables(conn, entities);
            System.out.println("Morning Checklist: Database tables created!");
            
            // Seed a default admin user if none exists
            GenericDao<User, Long> userDao = new GenericDao<>(User.class, dataSourceHelper);
            if (userDao.findAll().isEmpty()) {
                System.out.println("Morning Checklist: No users found. Creating default Admin user...");
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("AdminPass");
                admin.setRole("ADMIN");
                userDao.save(admin);
            }

            // Seed Hospital Equipments
            GenericDao<HospitalEquipment, Long> equipmentDao = new GenericDao<>(HospitalEquipment.class, dataSourceHelper);
            List<HospitalEquipment> existingEquipment = equipmentDao.findAll();
            
            // If the table is empty or only contains junk, seed the real ones
            if (existingEquipment.size() < 4) { 
                System.out.println("Morning Checklist: Seeding professional Medical Equipment...");
                
                // 1. X-Ray Machine (Standard - 6 months)
                HospitalEquipment xray = new HospitalEquipment();
                xray.setName("X-RAY MACHINE (GE Proteus)");
                xray.setSerialNumber("XR-GE-9900");
                xray.setStatus("Active");
                xray.setPurchaseDate(new java.util.Date(System.currentTimeMillis() - 15552000000L)); // 6 months ago
                xray.setLastCalibrationDate(xray.getPurchaseDate());
                xray.setNextCalibrationDate(standardMaintenance.calculateNextMaintenanceDate(xray.getLastCalibrationDate()));
                equipmentDao.save(xray);

                // 2. Ventilator (Urgent - 2 months)
                HospitalEquipment ventilator = new HospitalEquipment();
                ventilator.setName("VENTILATOR (Dräger Savina)");
                ventilator.setSerialNumber("VN-DR-1122");
                ventilator.setStatus("Active");
                ventilator.setPurchaseDate(new java.util.Date(System.currentTimeMillis() - 5184000000L)); // 2 months ago
                ventilator.setLastCalibrationDate(ventilator.getPurchaseDate());
                ventilator.setNextCalibrationDate(urgentMaintenance.calculateNextMaintenanceDate(ventilator.getLastCalibrationDate()));
                equipmentDao.save(ventilator);

                // 3. MRI Scanner (Standard - 6 months)
                HospitalEquipment mri = new HospitalEquipment();
                mri.setName("MRI SCANNER (Siemens Magnetom)");
                mri.setSerialNumber("MR-SI-4455");
                mri.setStatus("Active");
                mri.setPurchaseDate(new java.util.Date(System.currentTimeMillis() - 2592000000L)); // 1 month ago
                mri.setLastCalibrationDate(mri.getPurchaseDate());
                mri.setNextCalibrationDate(standardMaintenance.calculateNextMaintenanceDate(mri.getLastCalibrationDate()));
                equipmentDao.save(mri);

                // 4. Defibrillator (Urgent - 2 months)
                HospitalEquipment defibrillator = new HospitalEquipment();
                defibrillator.setName("DEFIBRILLATOR (Lifepak 15)");
                defibrillator.setSerialNumber("DF-LP-7788");
                defibrillator.setStatus("Active");
                defibrillator.setPurchaseDate(new java.util.Date(System.currentTimeMillis() - 4320000000L)); // ~50 days ago
                defibrillator.setLastCalibrationDate(defibrillator.getPurchaseDate());
                defibrillator.setNextCalibrationDate(urgentMaintenance.calculateNextMaintenanceDate(defibrillator.getLastCalibrationDate()));
                equipmentDao.save(defibrillator);

                // 5. Dialysis Machine (Urgent - 2 months)
                HospitalEquipment dialysis = new HospitalEquipment();
                dialysis.setName("DIALYSIS MACHINE (Fresenius 4008)");
                dialysis.setSerialNumber("DL-FR-5566");
                dialysis.setStatus("Active");
                dialysis.setPurchaseDate(new java.util.Date(System.currentTimeMillis() - 6048000000L)); // Over 2 months ago (Should be OVERDUE)
                dialysis.setLastCalibrationDate(dialysis.getPurchaseDate());
                dialysis.setNextCalibrationDate(urgentMaintenance.calculateNextMaintenanceDate(dialysis.getLastCalibrationDate()));
                equipmentDao.save(dialysis);
            }
            
        } catch (Exception e) {
            System.err.println("Morning Checklist: Failed to connect to database or seed data! " + e.getMessage());
            e.printStackTrace();
        }
    }
}
