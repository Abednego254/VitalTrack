package app.utility.bootstrap;

import app.ejb.UserEJB;
import app.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import java.util.List;

@Singleton
@Startup
public class DatabaseSeeder {

    @Inject
    private UserEJB userEJB;

    @PostConstruct
    public void seed() {
        System.out.println(">>> SEEDER: Checking for admin user...");

        // Check if users exist by fetching list
        // If empty, create the default admin
        try {
            // Note: In a production app we'd use a more efficient count query
            if (userEJB.authenticate("admin", "admin123") == null) {
                 User admin = new User();
                 admin.setUsername("admin");
                 admin.setPassword("admin123");
                 admin.setRole("ADMIN");
                 userEJB.save(admin);
                 System.out.println(">>> SEEDER: Default Admin user created!");
            } else {
                System.out.println(">>> SEEDER: Admin user already exists.");
            }
        } catch (Exception e) {
            System.err.println("!!! SEEDER ERROR: " + e.getMessage());
        }
    }
}
