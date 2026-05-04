package app.ejb;

import app.dao.GenericDao;
import app.model.User;
import app.utility.DataSourceHelper;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class UserEJB {

    @Inject
    private DataSourceHelper dataSourceHelper;

    private GenericDao<User, Long> userDao;

    @PostConstruct
    public void init() {
        this.userDao = new GenericDao<>(User.class, dataSourceHelper);
    }

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
