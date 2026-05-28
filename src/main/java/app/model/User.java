package app.model;

import jakarta.persistence.*; 
import app.framework.VitalTrackFormField;
import app.framework.VitalTrackTableCol;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_role", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("ADMIN")
public class User extends BaseEntity {

    @Column(nullable = false)
    @VitalTrackTableCol(label = "Name")
    @VitalTrackFormField(label = "Name", placeholder = "Enter full name")
    @jakarta.validation.constraints.NotBlank(message = "Name is required")
    @jakarta.validation.constraints.Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    protected String name;

    @Column(nullable = false, unique = true)
    @VitalTrackTableCol(label = "Email")
    @VitalTrackFormField(label = "Email Address", placeholder = "email@hospital.com")
    @jakarta.validation.constraints.NotBlank(message = "Email is required")
    @jakarta.validation.constraints.Email(message = "Please provide a valid email address")
    protected String email;

    // Password is system-generated for nurses/technicians — NOT shown in the add form.
    // Validation is skipped here; the EJB sets a temp password before saving.
    @Column
    protected String password;

    public User() {}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return email; }
    
    public void setUsername(String username) { this.email = username; }

    // Dynamic helper to return the discriminator value as the role
    @Transient
    public String getRole() {
        return "ADMIN";
    }
}