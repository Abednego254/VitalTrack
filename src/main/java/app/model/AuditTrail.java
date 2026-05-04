package app.model;

import app.framework.DbColumn;
import app.framework.DbTable;
import java.io.Serializable;
import java.util.Date;

@DbTable(name = "AuditTrail")
public class AuditTrail implements Serializable {

    @DbColumn(name = "id", type = "BIGINT", primaryKey = true, autoIncrement = true)
    private Long id;

    @DbColumn(name = "activity", type = "TEXT")
    private String activity;

    @DbColumn(name = "timestamp", type = "VARCHAR(255)")
    private String timestamp;

    public AuditTrail() {}

    public AuditTrail(String activity) {
        this.activity = activity;
        this.timestamp = new Date().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
