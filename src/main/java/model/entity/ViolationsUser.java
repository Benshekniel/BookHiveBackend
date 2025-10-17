package model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "violations_user")
public class ViolationsUser {

    @Id
    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "reason", nullable = true)
    private String reason;

    public ViolationsUser(String email, String reason) {
        this.email = email;
        this.reason = reason;
    }

    public ViolationsUser() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ViolationsUser{" +
                "email='" + email + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
