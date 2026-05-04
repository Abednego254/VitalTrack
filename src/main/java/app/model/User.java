package app.model;

import app.framework.DbColumn;
import app.framework.DbTable;
import java.io.Serializable;

@DbTable(name = "Users")
public class User implements Serializable {

    @DbColumn(name = "id", type = "BIGINT", primaryKey = true, autoIncrement = true)
    private Long id;

    @DbColumn(name = "username", type = "VARCHAR(255)")
    private String username;

    @DbColumn(name = "password", type = "VARCHAR(255)")
    private String password;

    @DbColumn(name = "role", type = "VARCHAR(50)")
    private String role;

    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
