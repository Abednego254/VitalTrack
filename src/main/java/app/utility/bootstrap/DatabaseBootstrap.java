package app.utility.bootstrap;

import app.model.HospitalEquipment;
import app.model.HospitalMaintenanceLog;
import app.model.HospitalMedicalSupply;
import app.model.HospitalTechnician;
import app.utility.DataSourceHelper;
import app.utility.db.TableGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

/**
 * [CONCEPT: Strategy Pattern Implementation]
 * This is one specific item on our morning checklist.
 * Its job is to make sure the database is ready and all tables exist.
 */
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

        // We give them to the Table Generator Builder
        try (Connection conn = dataSourceHelper.getConnection()) {
            TableGenerator.generateTables(conn, entities);
            System.out.println("Morning Checklist: Database is READY!");
        } catch (Exception e) {
            System.err.println("Morning Checklist: Failed to connect to database!");
        }
    }
}
