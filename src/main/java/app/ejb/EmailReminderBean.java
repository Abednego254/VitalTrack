package app.ejb;

import app.model.AuditTrail;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import java.util.Date;
import jakarta.annotation.Resource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * [CONCEPT: EJB Timer Service - @Schedule]
 * This is a robot worker that does a task automatically based on a clock.
 */
@Singleton
public class EmailReminderBean {

    @Resource(lookup = "java:jboss/mail/Default")
    private Session mailSession;

    @Inject
    private Event<AuditTrail> auditTrailEvent;

    /**
     * This method runs every 30 seconds automatically.
     * We set persistent = false so it doesn't try to remember missed tasks after
     * server restart.
     */
    @Schedule(second = "*/30", minute = "*", hour = "*", persistent = false)
    public void sendReminders() {
        System.out.println(">>> ROBOT: Constructing a real email reminder...");

        try {
            // 1. Create a empty message shell
            Message message = new MimeMessage(mailSession);

            // 2. Set the 'From' and 'To' addresses
            message.setFrom(new InternetAddress("alerts@vitaltrack.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("ydenzel158@gmail.com"));

            // 3. Set the Subject and Content
            message.setSubject("VitalTrack: Automated System Reminder");
            message.setText(
                    "Hello Admin,\n\nThis is an automated check from VitalTrack. The system is running smoothly as of: "
                            + new Date());

            // 4. THE BIG MOMENT: Send the email!
            Transport.send(message);

            System.out.println(">>> SUCCESS: Real email has been sent to the admin.");

            // We still fire our audit event so we have a database record of the email
            auditTrailEvent.fire(new AuditTrail("Automated reminder email sent to admin at: " + new Date()));

        } catch (MessagingException e) {
            System.out.println(">>> FAILURE: Could not send email. Error: " + e.getMessage());
        }
    }

}
