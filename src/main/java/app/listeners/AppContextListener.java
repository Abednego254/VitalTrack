package app.listeners;

import app.ejb.EmailReminderBean;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Inject
    private EmailReminderBean emailReminderBean;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("==================================================");
        System.out.println(" HOSPITAL MANAGER: Starting the morning routine...");
        System.out.println("==================================================");

        // Routine startup logs
        System.out.println(">>> AppContextListener: Hospital is starting...");

        // EXTRA: Send the daily report to technicians immediately on startup
        emailReminderBean.sendReminders();

        System.out.println("==================================================");
        System.out.println(" HOSPITAL MANAGER: All chores done. We are open!");
        System.out.println("==================================================");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("==================================================");
        System.out.println(" HOSPITAL MANAGER: Hospital is closing down...");
        System.out.println("==================================================");
    }
}
