package app.ejb;

import app.dao.UserDao;
import app.model.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class UserEJB {

    @Inject
    private UserDao userDao;

    public void save(User user) {
        userDao.save(user);
    }

    public User authenticate(String usernameOrEmail, String password) {
        // A simple authentication method. 
        // We fetch all users and find the match.
        List<User> users = userDao.findAll();
        for (User user : users) {
            if (user.getEmail() != null && user.getPassword() != null && user.getPassword().equals(password)) {
                String emailPart = user.getEmail().split("@")[0];
                if (user.getEmail().equalsIgnoreCase(usernameOrEmail) || emailPart.equalsIgnoreCase(usernameOrEmail)) {
                    return user; // Match found!
                }
            }
        }
        return null; // No match
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
