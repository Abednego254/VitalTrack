package app.listeners;

import app.utility.bootstrap.Bootstrap;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * [CONCEPT: WebListener Bootstrapping]
 * This is the Hospital Manager.
 * Instead of doing all the morning chores himself, he asks for EVERY
 * worker who implements the "Bootstrap" checklist interface.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    // [CONCEPT: Advanced CDI Injection]
    // @Any and Instance<T> tells CDI:
    // "Give me a list of EVERY class in the whole project that implements Bootstrap!"
    @Inject
    @Any
    private Instance<Bootstrap> bootstraps;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("==================================================");
        System.out.println(" HOSPITAL MANAGER: Starting the morning routine...");
        System.out.println("==================================================");

        // The manager goes through the list and tells each worker to do their chore
        for (Bootstrap bootstrap : bootstraps) {
            bootstrap.process();
        }
        
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
