package app.ejb;

import java.text.SimpleDateFormat;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import jakarta.annotation.Resource;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/VitalTrackAppQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue")
    }
)
public class ExternalAuditServerBean implements MessageListener {

    @Resource(lookup = "java:jboss/mail/Default")
    private Session mailSession;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMsg = (TextMessage) message;
            String activity = textMsg.getText();
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            String logEntry = String.format("[%s] BACKUP: %s%n", timestamp, activity);

            java.nio.file.Path backupPath = java.nio.file.Paths.get("/tmp", "vitaltrack_external_backup.log");
            
            java.nio.file.Files.write(
                backupPath, 
                logEntry.getBytes(), 
                java.nio.file.StandardOpenOption.CREATE, 
                java.nio.file.StandardOpenOption.APPEND
            );

            System.out.println("==================================================");
            System.out.println(" 💾 EXTERNAL BACKUP SERVER: Activity Persisted!");
            System.out.println(" FILE: " + backupPath.toAbsolutePath());
            System.out.println("==================================================");

            if (activity.startsWith("STOCK ALERT")) {
                sendUrgentEmail(activity);
            }
            
        } catch (Exception e) {
            System.err.println("!!! JMS BACKUP FAILURE: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendUrgentEmail(String alertText) {
        try {
            jakarta.mail.Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("alerts@vitaltrack.com"));
            message.setRecipients(jakarta.mail.Message.RecipientType.TO, InternetAddress.parse("ydenzel158@gmail.com"));
            message.setSubject("URGENT: Store Room Stock Level Alert");
            message.setText("VitalTrack Automated Monitor Alert:\n\n" + alertText + 
                          "\n\nPlease replenish these supplies immediately to avoid shortages.\n\nRegards,\nVitalTrack External Monitor");
            
            Transport.send(message);
            System.out.println(">>> SUCCESS: Urgent Stock Alert Email Sent!");
        } catch (Exception e) {
            System.err.println("!!! FAILED TO SEND URGENT STOCK EMAIL: " + e.getMessage());
        }
    }
}
