package app.ejb;

import app.model.AuditTrail;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Observes;

/**
 * [CONCEPT: CDI Events - Multiple Observers]
 * This Singleton worker also listens to the intercom.
 * When an AuditTrail event is fired, both AuditTrailBean and this bean will react!
 */
@Singleton
public class NotificationBean {

    public void onAuditTrailEvent(@Observes AuditTrail auditTrail) {
        System.out.println(">>> NOTIFICATION SERVICE: Sending alert for activity: " + auditTrail.getActivity());
        // In a real app, this would send an actual email or SMS
        System.out.println(">>> NOTIFICATION SERVICE: Email sent to administrator.");
    }
}
