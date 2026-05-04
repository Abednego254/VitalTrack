package app.ejb;

import app.dao.GenericDao;
import app.model.AuditTrail;
import app.utility.DataSourceHelper;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@Singleton
public class AuditTrailBean {

    @Inject
    private DataSourceHelper dataSourceHelper;

    private GenericDao<AuditTrail, Long> auditTrailDao;

    @PostConstruct
    public void init() {
        this.auditTrailDao = new GenericDao<>(AuditTrail.class, dataSourceHelper);
        System.out.println("********** AuditTrailBean (@Singleton) Initialized! **********");
    }

    /**
     * [CONCEPT: CDI Events - @Observes]
     * Instead of being called directly, this method listens for an event.
     * When any other bean fires an AuditTrail event, this method catches it and saves it.
     */
    public void recordEvent(@Observes AuditTrail auditTrail) {
        System.out.println(">>> THE INTERCOM: Received new Audit Trail Event: " + auditTrail.getActivity());
        auditTrailDao.save(auditTrail);
    }
}
