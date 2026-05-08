package app.ejb;

import java.text.SimpleDateFormat;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

/**
 * [CONCEPT: Message Driven Bean (MDB)]
 * This is our "Listener" or "Consumer". 
 * It doesn't have a UI or a URL. 
 * It simply sits and waits for a message to arrive in the "VitalTrackAppQueue".
 */
@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/VitalTrackAppQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue")
    }
)
public class ExternalAuditServerBean implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMsg = (TextMessage) message;
            String activity = textMsg.getText();
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            String logEntry = String.format("[%s] BACKUP: %s%n", timestamp, activity);

            // We write to a file in my home directory to simulate a real backup
            // Using StandardOpenOption.APPEND to keep a history of all backups
            // Using /tmp to ensure the wildfly user has write permissions regardless of who started it
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
            
        } catch (Exception e) {
            System.err.println("!!! JMS BACKUP FAILURE: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
