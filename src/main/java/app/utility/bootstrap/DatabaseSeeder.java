package app.utility.bootstrap;

import app.ejb.UserEJB;
import app.dao.UserDao;
import app.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.*;
import jakarta.inject.Inject;

@Singleton
@Startup
public class DatabaseSeeder {

    @Inject
    private UserEJB userEJB;

    @Inject
    private UserDao userDao;

    @PostConstruct
    public void seed() {
        System.out.println(">>> SEEDER: Checking for admin user...");

        // Check if users exist by fetching list
        // If empty, create the default admin
        try {
            if (userEJB.authenticate("admin@hospital.com", "admin123") == null) {
                 User admin = new User();
                 admin.setName("System Admin");
                 admin.setEmail("admin@hospital.com");
                 admin.setPassword("admin123");
                 userDao.save(admin);
                 System.out.println(">>> SEEDER: Default Admin user created!");
            } else {
                System.out.println(">>> SEEDER: Admin user already exists.");
            }
        } catch (Exception e) {
            System.err.println("!!! SEEDER ERROR: " + e.getMessage());
        }
    }
}
