package app.dao;

import app.model.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserDao extends GenericDao<User, Long> {
}
