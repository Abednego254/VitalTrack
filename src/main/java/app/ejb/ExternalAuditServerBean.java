package app.ejb;

import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

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

            java.nio.file.Path backupPath = java.nio.file.Paths.get("/tmp", "vitaltrack_external_backup.log");
            
            Files.write(
                backupPath, 
                logEntry.getBytes(), 
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
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
