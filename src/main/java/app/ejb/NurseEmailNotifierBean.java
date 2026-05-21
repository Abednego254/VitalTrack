package app.ejb;

import app.model.NurseAddedEvent;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Observes;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Singleton
public class NurseEmailNotifierBean {

    @Resource(lookup = "java:jboss/mail/Default")
    private Session mailSession;

    public void onNurseAdded(@Observes NurseAddedEvent event) {
        System.out.println(">>> ROBOT: New nurse added! Preparing welcome email for " + event.getNurse().getEmail() + " with temporary password: " + event.getDummyPassword());

        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("alerts@vitaltrack.com"));

            if (event.getNurse().getEmail() == null || event.getNurse().getEmail().trim().isEmpty()) {
                System.out.println(">>> WARNING: Nurse has no email address. Skipping welcome email.");
                return;
            }

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(event.getNurse().getEmail()));
            message.setSubject("Welcome to VitalTrack Nursing Team");

            StringBuilder body = new StringBuilder();
            body.append("Hello ").append(event.getNurse().getName()).append(",\n\n");
            body.append("Welcome to the VitalTrack Nursing Team!\n\n");
            body.append("Your account has been successfully created. Please use the following temporary password to log in for the first time:\n\n");
            body.append("Temporary Password: ").append(event.getDummyPassword()).append("\n\n");
            body.append("You will be required to change this password immediately upon your first login.\n\n");
            body.append("Regards,\nVitalTrack Administration");

            message.setText(body.toString());

            Transport.send(message);

            System.out.println(">>> SUCCESS: Welcome email sent to " + event.getNurse().getEmail());

        } catch (Exception e) {
            System.err.println(">>> FAILURE: Could not send welcome email to " + event.getNurse().getEmail() + ". Error: " + e.getMessage());
        }
    }
}
