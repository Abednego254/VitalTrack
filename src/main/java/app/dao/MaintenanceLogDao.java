package app.dao;

import app.model.MaintenanceLog;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MaintenanceLogDao extends GenericDao<MaintenanceLog, Long> {
}
