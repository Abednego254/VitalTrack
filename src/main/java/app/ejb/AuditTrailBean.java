package app.ejb;

import app.dao.AuditTrailDao;
import app.model.AuditTrail;
import app.websocket.AuditTrailWs;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import java.util.List;

@Singleton
public class AuditTrailBean {

    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = "java:/jms/queue/VitalTrackAppQueue")
    private Queue auditQueue;

    @Inject
    private AuditTrailDao auditTrailDao;
    public void recordEvent(@Observes AuditTrail auditTrail) {
        System.out.println(">>> THE INTERCOM: Received new Audit Trail Event: " + auditTrail.getAction());
        
        // 1. Save to our local database
        auditTrailDao.save(auditTrail);

        // 2. Send to External Server via JMS (The "Producer")
        jmsContext.createProducer().send(auditQueue, auditTrail.getAction());

        // 3. Broadcast real-time live system activity to Admins via WebSocket
        AuditTrailWs.broadcast(auditTrail.getAction());
    }
    public List<AuditTrail> findAll() {
        return auditTrailDao.findAll();
    }
    public void delete(Long id) {
        auditTrailDao.delete(id);
    }
}
