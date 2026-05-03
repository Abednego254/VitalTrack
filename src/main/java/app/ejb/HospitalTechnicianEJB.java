package app.ejb;

import app.model.HospitalTechnician;
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
 * The dedicated Database Specialist for Technicians.
 * WildFly manages transactions automatically for every method here.
 */
@Stateless
public class HospitalTechnicianEJB {

    @Inject
    private DataSourceHelper dataSourceHelper;

    public void save(HospitalTechnician technician) throws Exception {
        String sql = "INSERT INTO HospitalTechnician (name, specialization, contactInfo, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSourceHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, technician.getName());
            stmt.setString(2, technician.getSpecialization());
            stmt.setString(3, technician.getContactInfo());
            stmt.setString(4, technician.getStatus());
            stmt.executeUpdate();

            System.out.println("*** TECHNICIAN EJB: Saved [" + technician.getName() + "] to vault! ***");
        }
    }

    public List<HospitalTechnician> findAll() throws Exception {
        List<HospitalTechnician> list = new ArrayList<>();
        String sql = "SELECT * FROM HospitalTechnician";

        try (Connection conn = dataSourceHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HospitalTechnician tech = new HospitalTechnician();
                tech.setId(rs.getLong("id"));
                tech.setName(rs.getString("name"));
                tech.setSpecialization(rs.getString("specialization"));
                tech.setContactInfo(rs.getString("contactInfo"));
                tech.setStatus(rs.getString("status"));
                list.add(tech);
            }
        }
        return list;
    }
}
