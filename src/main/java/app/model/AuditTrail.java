package app.model;

import app.framework.VitalTrackTable;
import app.framework.VitalTrackTableCol;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "audit_trail")
@VitalTrackTable(label = "Security Audit Trail", deleteLink = "audit-trail/delete")
public class AuditTrail extends BaseEntity {

    @Column
    @VitalTrackTableCol(label = "Action")
    private String action;

    @Column(columnDefinition = "TEXT")
    @VitalTrackTableCol(label = "Details")
    private String details;

    @Column(name = "performed_by")
    @VitalTrackTableCol(label = "Performed By")
    private String performedBy;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @VitalTrackTableCol(label = "Timestamp")
    private Date timestamp;

    public AuditTrail() {}

    public AuditTrail(String action) {
        this.action = action;
        this.details = action;
        this.performedBy = "System";
        this.timestamp = new Date();
    }

    public AuditTrail(String action, String details, String performedBy) {
        this.action = action;
        this.details = details;
        this.performedBy = performedBy;
        this.timestamp = new Date();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
