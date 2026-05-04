package app.ejb;

import app.model.AuditTrail;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import java.util.Date;

/**
 * [CONCEPT: EJB Timer Service - @Schedule]
 * This is a robot worker that does a task automatically based on a clock.
 */
@Singleton
public class EmailReminderBean {

    @Inject
    private Event<AuditTrail> auditTrailEvent;

    /**
     * This method runs every 30 seconds automatically.
     * We set persistent = false so it doesn't try to remember missed tasks after server restart.
     */
    @Schedule(second = "*/30", minute = "*", hour = "*", persistent = false)
    public void sendReminders() {
        System.out.println(">>> SCHEDULED TASK: Checking for pending medical supply reminders...");
        
        // We notify the system that a scheduled check happened
        auditTrailEvent.fire(new AuditTrail("Automated system check performed at: " + new Date()));
    }
}
