package app.ejb;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

/**
 * [CONCEPT: Message Driven Bean (MDB)]
 *
 * This bean is a "Consumer". It does not wait for a user request.
 * It just sits and listens to the "VitalTrackAppQueue".
 * 
 * When a message arrives, the container (WildFly) calls onMessage() automatically.
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
            // We cast the message to TextMessage because we sent a String
            TextMessage textMsg = (TextMessage) message;
            
            System.out.println("==================================================");
            System.out.println(" 📟 EXTERNAL BACKUP SERVER: Message Received!");
            System.out.println(" CONTENT: " + textMsg.getText());
            System.out.println(" STATUS: Backup successfully persisted in Remote Storage.");
            System.out.println("==================================================");
            
        } catch (Exception e) {
            System.err.println("!!! EXTERNAL SERVER ERROR: " + e.getMessage());
        }
    }
}
