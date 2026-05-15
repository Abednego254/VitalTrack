package app.dao;

import app.model.AuditTrail;
import jakarta.enterprise.context.Dependent;

@Dependent
public class AuditTrailDao extends GenericDao<AuditTrail, Long> {
}
