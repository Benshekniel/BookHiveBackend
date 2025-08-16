package model.entity;

import jakarta.persistence.*;

@Entity
@Table(name="All_Users")
public class AllUsers {

    @Id
    @Column(name="user_id", length = 15)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int user_id;
    @Column(name="name", length = 255)
    private String name;
    @Column(name="email", length = 255)
    private String email;
    @Column(name="password", length = 255)
    private String password;
    @Column(name="role", length = 255)
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = true)
    private Status status;

    public enum Status {
        active, pending, banned, disabled, rejected
    }

    //Without status
    public AllUsers(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    //With status
    public AllUsers(String name, String email, String password, String role, Status status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public AllUsers() {
    }

    public int getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Status getStatus() {
        return status;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AllUsers{" +
                "user_id=" + user_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", status=" + status +
                '}';
    }
}
