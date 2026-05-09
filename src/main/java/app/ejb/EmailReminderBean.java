package app.ejb;

import app.dao.GenericDao;
import app.model.AuditTrail;
import app.model.HospitalEquipment;
import app.utility.DataSourceHelper;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.annotation.Resource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Singleton
public class EmailReminderBean {

    @Resource(lookup = "java:jboss/mail/Default")
    private Session mailSession;

    @Inject
    private Event<AuditTrail> auditTrailEvent;

    @Inject
    private DataSourceHelper dataSourceHelper;

    private GenericDao<HospitalEquipment, Long> equipmentDao;

    @PostConstruct
    public void init() {
        this.equipmentDao = new GenericDao<>(HospitalEquipment.class, dataSourceHelper);
    }

    @Schedule(second = "0", minute = "*/5", hour = "*", persistent = false)
    public void sendReminders() {
        System.out.println(">>> ROBOT: Scanning database for equipment due for maintenance...");

        List<HospitalEquipment> allEquipment = equipmentDao.findAll();
        
        // Find equipment due today or in the past (Overdue)
        List<HospitalEquipment> dueEquipment = allEquipment.stream()
                .filter(e -> e.getNextCalibrationDate() != null && e.getNextCalibrationDate().before(new Date(System.currentTimeMillis() + 86400000))) // Within next 24h
                .collect(Collectors.toList());

        if (dueEquipment.isEmpty()) {
            System.out.println(">>> ROBOT: No equipment due for maintenance today. Sending 'All Clear' report...");
            sendAllClearEmail();
            return;
        }

        System.out.println(">>> ROBOT: Found " + dueEquipment.size() + " items needing maintenance! Constructing email...");

        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("alerts@vitaltrack.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("ydenzel158@gmail.com"));
            message.setSubject("URGENT: Hospital Equipment Maintenance Required");

            StringBuilder body = new StringBuilder();
            body.append("Hello Maintenance Team,\n\n");
            body.append("The following hospital equipment is due for calibration/maintenance today:\n\n");

            for (HospitalEquipment e : dueEquipment) {
                body.append("- ").append(e.getName())
                    .append(" (S/N: ").append(e.getSerialNumber()).append(")")
                    .append(" | Due Date: ").append(e.getNextCalibrationDate())
                    .append("\n");
            }

            body.append("\nPlease prioritize these items to ensure patient safety.\n\n");
            body.append("Regards,\nVitalTrack Automated Monitor");

            message.setText(body.toString());

            Transport.send(message);

            System.out.println(">>> SUCCESS: Maintenance alert sent for " + dueEquipment.size() + " items.");
            auditTrailEvent.fire(new AuditTrail("Maintenance reminder sent for " + dueEquipment.size() + " equipments."));

        } catch (MessagingException e) {
            System.out.println(">>> FAILURE: Could not send maintenance email. Error: " + e.getMessage());
        }
    }

    private void sendAllClearEmail() {
        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("alerts@vitaltrack.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("ydenzel158@gmail.com"));
            message.setSubject("VitalTrack: Daily Maintenance Status [All Clear]");

            message.setText("Hello Maintenance Team,\n\n" +
                    "This is your daily status report. All hospital equipment is currently within its calibration period.\n" +
                    "No immediate maintenance actions are required for today.\n\n" +
                    "Have a productive day!\n" +
                    "Regards,\nVitalTrack Automated Monitor");

            Transport.send(message);
            System.out.println(">>> SUCCESS: 'All Clear' report sent to the team.");

        } catch (MessagingException e) {
            System.out.println(">>> FAILURE: Could not send 'All Clear' email. Error: " + e.getMessage());
        }
    }
}
