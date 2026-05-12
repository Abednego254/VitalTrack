package app.dao;

import app.model.AuditTrail;
import app.utility.DataSourceHelper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class AuditTrailDao extends GenericDao<AuditTrail, Long> {

    @Inject
    public AuditTrailDao(DataSourceHelper ds) {
        super(AuditTrail.class);
        setDs(ds);
    }
}
