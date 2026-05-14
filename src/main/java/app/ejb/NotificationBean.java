package app.ejb;

import app.model.AuditTrail;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Observes;

@Singleton
public class NotificationBean {

    public void onAuditTrailEvent(@Observes AuditTrail auditTrail) {
        System.out.println(">>> NOTIFICATION SERVICE: Sending alert for activity: " + auditTrail.getAction());

        System.out.println(">>> NOTIFICATION SERVICE: Email sent to administrator.");
    }
}
