package app.ejb;

import app.model.AuditTrail;
import app.model.HospitalMedicalSupply;
import app.model.MedicalSupplyConsumedEvent;
import app.websocket.StockAlertWs;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

@Singleton
public class StockMonitorBean {

    @EJB
    private HospitalMedicalSupplyEJB supplyEJB;

    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = "java:/jms/queue/VitalTrackAppQueue")
    private Queue alertQueue;

    @Inject
    private Event<AuditTrail> auditTrailEvent;

    public void onSupplyConsumed(@Observes MedicalSupplyConsumedEvent event) {
        System.out.println(">>> STOCK MONITOR: Processing consumption for " + event.getSupplyName());

        try {
            HospitalMedicalSupply supply = supplyEJB.findById(event.getSupplyId());
            if (supply != null) {
                int newQty = supply.getQuantity() - event.getQuantityConsumed();
                supply.setQuantity(Math.max(0, newQty));
                supplyEJB.update(supply);

                System.out.println(">>> STOCK MONITOR: New quantity for " + supply.getName() + " is " + supply.getQuantity());

                // Fire Audit Trail event for supply consumption
                auditTrailEvent.fire(new AuditTrail("Consumed " + event.getQuantityConsumed() + " units of " + supply.getName() + " (Remaining: " + supply.getQuantity() + ")"));

                // ALERT CHECK: If quantity drops below reorder level, notify via JMS
                if (supply.getQuantity() <= supply.getReorderLevel()) {
                    String alertMessage = "STOCK ALERT: " + supply.getName() + " is running low! Current stock: " + supply.getQuantity();
                    System.out.println(">>> STOCK MONITOR: Triggering external alert for " + supply.getName());
                    
                    // Sending to JMS Queue for the MDB to handle the notification/backup
                    jmsContext.createProducer().send(alertQueue, alertMessage);

                    // Real-Time WebSocket broadcast to Admins and Nurses
                    StockAlertWs.broadcast("Warning: Medical Supply '" + supply.getName() + "' is running low! Current stock: " + supply.getQuantity());

                    // Fire Audit Trail event for low stock warning
                    auditTrailEvent.fire(new app.model.AuditTrail("CRITICAL STOCK WARNING: " + supply.getName() + " is running low (Stock: " + supply.getQuantity() + ")"));
                }
            }
        } catch (Exception e) {
            System.err.println("!!! STOCK MONITOR FAILURE: " + e.getMessage());
        }
    }
}
