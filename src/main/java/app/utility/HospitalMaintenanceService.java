package app.utility;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Calendar;
import java.util.Date;

@ApplicationScoped
public class HospitalMaintenanceService {
    public Date calculateNextMaintenanceDate(Date lastCalibrationDate) {
        if(lastCalibrationDate == null){
            return new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastCalibrationDate);
        calendar.add(Calendar.MONTH, 6);
        return calendar.getTime();
    }
}
