package app.ejb;

import app.dao.UserDao;
import app.model.AuditTrail;
import app.model.User;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
@DeclareRoles({"ADMIN", "TECHNICIAN", "NURSE"})
@PermitAll
public class UserEJB {

    @Inject
    private Event<AuditTrail> auditTrailEvent;

    @Inject
    private UserDao userDao;
    
    public void save(User user) {
        userDao.save(user);
    }

    /**
     * Records a logout audit event. Called by LoginAction so the
     * web tier doesn't fire business events directly.
     */
    public void logout(String username) {
        auditTrailEvent.fire(new AuditTrail("User '" + username + "' logged out."));
    }

    public User authenticate(String usernameOrEmail, String password) {
        
        List<User> users = userDao.findAll();
        for (User user : users) {
            if (user.getEmail() != null && user.getPassword() != null && user.getPassword().equals(password)) {
                String emailPart = user.getEmail().split("@")[0];
                if (user.getEmail().equalsIgnoreCase(usernameOrEmail) || emailPart.equalsIgnoreCase(usernameOrEmail)) {
                    return user;
                }
            }
        }
        return null;
    }

    public User findByEmail(String email) {
        if (email == null) {
            return null;
        }
        List<User> users = userDao.findAll();
        for (User user : users) {
            if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
}
