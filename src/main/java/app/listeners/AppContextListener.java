package app.listeners;

import app.ejb.EmailReminderBean;
import app.utility.bootstrap.Bootstrap;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Inject
    @Any
    private Instance<Bootstrap> bootstraps;

    @Inject
    private EmailReminderBean emailReminderBean;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("==================================================");
        System.out.println(" HOSPITAL MANAGER: Starting the morning routine...");
        System.out.println("==================================================");

        // The manager goes through the list and tells each worker to do their chore
        for (Bootstrap bootstrap : bootstraps) {
            bootstrap.process();
        }

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
