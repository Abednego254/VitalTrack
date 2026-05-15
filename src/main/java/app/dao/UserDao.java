package app.dao;

import app.model.User;
import jakarta.enterprise.context.Dependent;

@Dependent
public class UserDao extends GenericDao<User, Long> {
}
