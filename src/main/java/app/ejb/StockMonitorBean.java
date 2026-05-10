package app.ejb;

import app.model.HospitalMedicalSupply;
import app.model.MedicalSupplyConsumedEvent;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
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

    public void onSupplyConsumed(@Observes MedicalSupplyConsumedEvent event) {
        System.out.println(">>> STOCK MONITOR: Processing consumption for " + event.getSupplyName());

        try {
            HospitalMedicalSupply supply = supplyEJB.findById(event.getSupplyId());
            if (supply != null) {
                int newQty = supply.getQuantity() - event.getQuantityConsumed();
                supply.setQuantity(Math.max(0, newQty));
                supplyEJB.update(supply);

                System.out.println(">>> STOCK MONITOR: New quantity for " + supply.getName() + " is " + supply.getQuantity());

                // ALERT CHECK: If quantity drops below reorder level, notify via JMS
                if (supply.getQuantity() <= supply.getReorderLevel()) {
                    String alertMessage = "STOCK ALERT: " + supply.getName() + " is running low! Current stock: " + supply.getQuantity();
                    System.out.println(">>> STOCK MONITOR: Triggering external alert for " + supply.getName());
                    
                    // Sending to JMS Queue for the MDB to handle the notification/backup
                    jmsContext.createProducer().send(alertQueue, alertMessage);
                }
            }
        } catch (Exception e) {
            System.err.println("!!! STOCK MONITOR FAILURE: " + e.getMessage());
        }
    }
}
