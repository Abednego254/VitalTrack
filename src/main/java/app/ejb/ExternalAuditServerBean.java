package app.ejb;

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
            // We assume the message is text
            TextMessage textMsg = (TextMessage) message;
            
            System.out.println("==================================================");
            System.out.println(" 🛰️  EXTERNAL BACKUP SERVER: Receiving Backup...");
            System.out.println(" DATA: " + textMsg.getText());
            System.out.println("==================================================");
            
        } catch (Exception e) {
            System.err.println("Error processing JMS message: " + e.getMessage());
        }
    }
}
