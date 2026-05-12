package app.ejb;

import app.model.TechnicianAddedEvent;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Observes;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Singleton
public class TechnicianEmailNotifierBean {

    @Resource(lookup = "java:jboss/mail/Default")
    private Session mailSession;

    public void onTechnicianAdded(@Observes TechnicianAddedEvent event) {
        System.out.println(">>> ROBOT: New technician added! Preparing welcome email for " + event.getTechnician().getEmail());

        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("alerts@vitaltrack.com"));
            
            // Only attempt to send if email is provided
            if (event.getTechnician().getEmail() == null || event.getTechnician().getEmail().trim().isEmpty()) {
                System.out.println(">>> WARNING: Technician has no email address. Skipping welcome email.");
                return;
            }
            
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(event.getTechnician().getEmail()));
            message.setSubject("Welcome to VitalTrack Technical Team");

            StringBuilder body = new StringBuilder();
            body.append("Hello ").append(event.getTechnician().getName()).append(",\n\n");
            body.append("Welcome to the VitalTrack Technical Team!\n\n");
            body.append("Your account has been successfully created. Please use the following temporary password to log in for the first time:\n\n");
            body.append("Temporary Password: ").append(event.getDummyPassword()).append("\n\n");
            body.append("You will be required to change this password immediately upon your first login.\n\n");
            body.append("Regards,\nVitalTrack Administration");

            message.setText(body.toString());

            Transport.send(message);

            System.out.println(">>> SUCCESS: Welcome email sent to " + event.getTechnician().getEmail());

        } catch (Exception e) {
            System.err.println(">>> FAILURE: Could not send welcome email to " + event.getTechnician().getEmail() + ". Error: " + e.getMessage());
        }
    }
}
