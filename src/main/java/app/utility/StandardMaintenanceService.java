package app.utility;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Calendar;
import java.util.Date;

@ApplicationScoped
@MaintenanceQualifier(MaintenanceChoice.STANDARD)
public class StandardMaintenanceService implements MaintenanceService {
    @Override
    public Date calculateNextMaintenanceDate(Date lastMaintenanceDate) {
        if(lastMaintenanceDate == null) return new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastMaintenanceDate);
        calendar.add(Calendar.MONTH, 6);
        return calendar.getTime();
    }
}
