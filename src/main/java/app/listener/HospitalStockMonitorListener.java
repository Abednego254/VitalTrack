package app.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.net.http.WebSocket;
@WebListener
public class HospitalStockMonitorListener implements ServletContextListener {
    @jakarta.inject.Inject
    private app.utility.DatabaseInitializer dbInitializer;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("GOOD MORNING!! HOSPITAL IS OPENING......");
        
        // Initialize the Database Tables
        dbInitializer.initialize();

        System.out.println("Scanning the available stock....");
        // Implement stock check here

        System.out.println("Stock check complete. All bandages are safe!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("GOOD NIGHT. HOSPITAL CLOSING!!");
    }
}
