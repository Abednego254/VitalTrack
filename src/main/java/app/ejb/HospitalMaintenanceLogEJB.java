package app.ejb;

import app.model.HospitalMaintenanceLog;
import app.utility.DataSourceHelper;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * [CONCEPT: @Stateless EJB]
 * The dedicated Database Specialist for Maintenance Logs.
 * WildFly manages transactions automatically for every method here.
 */
@Stateless
public class HospitalMaintenanceLogEJB {

    @Inject
    private DataSourceHelper dataSourceHelper;

    public void save(HospitalMaintenanceLog log) throws Exception {
        String sql = "INSERT INTO HospitalMaintenanceLog (equipmentId, technicianId, serviceDate, actionTaken, notes) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSourceHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, log.getEquipmentId());
            stmt.setObject(2, log.getTechnicianId());
            stmt.setObject(3, log.getServiceDate());
            stmt.setString(4, log.getActionTaken());
            stmt.setString(5, log.getNotes());
            stmt.executeUpdate();

            System.out.println("*** MAINTENANCE EJB: Log saved to vault! ***");
        }
    }

    public List<HospitalMaintenanceLog> findAll() throws Exception {
        List<HospitalMaintenanceLog> list = new ArrayList<>();
        String sql = "SELECT * FROM HospitalMaintenanceLog";

        try (Connection conn = dataSourceHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HospitalMaintenanceLog log = new HospitalMaintenanceLog();
                log.setId(rs.getLong("id"));
                log.setEquipmentId(rs.getLong("equipmentId"));
                log.setTechnicianId(rs.getLong("technicianId"));
                log.setServiceDate(rs.getDate("serviceDate"));
                log.setActionTaken(rs.getString("actionTaken"));
                log.setNotes(rs.getString("notes"));
                list.add(log);
            }
        }
        return list;
    }
}
