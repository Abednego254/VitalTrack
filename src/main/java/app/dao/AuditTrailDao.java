package app.dao;

import app.model.AuditTrail;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuditTrailDao extends GenericDao<AuditTrail, Long> {
}
