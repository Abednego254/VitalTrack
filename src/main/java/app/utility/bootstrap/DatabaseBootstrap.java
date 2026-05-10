package app.utility.bootstrap;

import app.model.AuditTrail;
import app.model.HospitalEquipment;
import app.model.HospitalMaintenanceLog;
import app.model.HospitalMedicalSupply;
import app.model.HospitalTechnician;
import app.model.User;
import app.utility.DataSourceHelper;
import app.utility.db.TableGenerator;
import app.dao.GenericDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class DatabaseBootstrap implements Bootstrap {

    @Inject
    private DataSourceHelper dataSourceHelper;

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
            
        } catch (Exception e) {
            System.err.println("Morning Checklist: Failed to connect to database or seed users! " + e.getMessage());
            e.printStackTrace();
        }
    }
}
