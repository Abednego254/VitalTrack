package app.dao;

import app.model.User;
import app.utility.DataSourceHelper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class UserDao extends GenericDao<User, Long> {

    @Inject
    public UserDao(DataSourceHelper ds) {
        super(User.class);
        setDs(ds);
    }
}
