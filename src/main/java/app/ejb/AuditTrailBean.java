package app.ejb;

import app.dao.GenericDao;
import app.model.AuditTrail;
import app.utility.DataSourceHelper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

@Singleton
public class AuditTrailBean {

    @Inject
    private DataSourceHelper dataSourceHelper;

    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = "java:/jms/queue/VitalTrackAppQueue")
    private Queue auditQueue;

    private GenericDao<AuditTrail, Long> auditTrailDao;

    @PostConstruct
    public void init() {
        this.auditTrailDao = new GenericDao<>(AuditTrail.class, dataSourceHelper);
        System.out.println("********** AuditTrailBean (@Singleton) Initialized! **********");
    }

    public void recordEvent(@Observes AuditTrail auditTrail) {
        System.out.println(">>> THE INTERCOM: Received new Audit Trail Event: " + auditTrail.getActivity());
        
        // 1. Save to our local database
        auditTrailDao.save(auditTrail);

        // 2. Send to External Server via JMS (The "Producer")
        jmsContext.createProducer().send(auditQueue, auditTrail.getActivity());
    }
}
