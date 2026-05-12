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

    public User authenticate(String username, String password) {
        // A simple authentication method. 
        // We fetch all users and find the match.
        // In a real app, GenericDao would have a method like findBy("username", username)
        List<User> users = userDao.findAll();
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u; // Match found!
            }
        }
        return null; // No match
    }
}
