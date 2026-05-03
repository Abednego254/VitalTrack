package app.ejb;

import app.model.HospitalMedicalSupply;
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
 * The dedicated Database Specialist for Medical Supplies.
 * WildFly manages transactions automatically for every method here.
 */
@Stateless
public class HospitalMedicalSupplyEJB {

    @Inject
    private DataSourceHelper dataSourceHelper;

    public void save(HospitalMedicalSupply supply) throws Exception {
        String sql = "INSERT INTO HospitalMedicalSupply (name, category, quantity, unitOfMeasure, reorderLevel) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSourceHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supply.getName());
            stmt.setString(2, supply.getCategory());
            stmt.setInt(3, supply.getQuantity());
            stmt.setString(4, supply.getUnitOfMeasure());
            stmt.setInt(5, supply.getReorderLevel());
            stmt.executeUpdate();

            System.out.println("*** SUPPLY EJB: Saved [" + supply.getName() + "] to vault! ***");
        }
    }

    public List<HospitalMedicalSupply> findAll() throws Exception {
        List<HospitalMedicalSupply> list = new ArrayList<>();
        String sql = "SELECT * FROM HospitalMedicalSupply";

        try (Connection conn = dataSourceHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HospitalMedicalSupply supply = new HospitalMedicalSupply();
                supply.setId(rs.getLong("id"));
                supply.setName(rs.getString("name"));
                supply.setCategory(rs.getString("category"));
                supply.setQuantity(rs.getInt("quantity"));
                supply.setUnitOfMeasure(rs.getString("unitOfMeasure"));
                supply.setReorderLevel(rs.getInt("reorderLevel"));
                list.add(supply);
            }
        }
        return list;
    }
}
