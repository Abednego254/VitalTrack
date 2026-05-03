package app.ejb;

import app.model.HospitalEquipment;
import app.utility.DataSourceHelper;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class HospitalEquipmentEJB {
    @Inject
    private DataSourceHelper dataSourceHelper;

    public void save(HospitalEquipment hospitalEquipment) throws Exception {
        String sql = "INSERT INTO HospitalEquipment (name, serialNumber, status) VALUES (?, ?, ?)";

        try(Connection connection = dataSourceHelper.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, hospitalEquipment.getName());
            preparedStatement.setString(2, hospitalEquipment.getSerialNumber());
            preparedStatement.setString(3, hospitalEquipment.getStatus());
            preparedStatement.executeUpdate();
            System.out.println("*** EQUIPMENT EJB: Saved [" + hospitalEquipment.getName() + "] to vault! ***");
        }
    }

    public List<HospitalEquipment> findAll() throws Exception {
        List<HospitalEquipment> list = new ArrayList<>();
        String sql = "SELECT * FROM HospitalEquipment";

        try(Connection connection = dataSourceHelper.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                HospitalEquipment hospitalEquipment = new HospitalEquipment();
                hospitalEquipment.setId(resultSet.getLong("id"));
                hospitalEquipment.setName(resultSet.getString("name"));
                hospitalEquipment.setSerialNumber(resultSet.getString("serialNumber"));
                hospitalEquipment.setStatus(resultSet.getString("status"));
                list.add(hospitalEquipment);
            }
        }
        return list;
    }
}