package app.utility;

import java.util.Date;

public interface MaintenanceService {
    Date calculateNextMaintenanceDate(Date lastMaintenanceDate);
}
