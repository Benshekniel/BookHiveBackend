package model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="all_users")
public class AllUsers {

    @Id
    @Column(name="user_id", length = 15)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer user_id;

//    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

//    @Column(nullable = false)
    private String phoneNumber;

//    @Column(nullable = false)
    private String address;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String role;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public AllUsers(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public AllUsers() {
    }

//    public int getUser_id() {
//        return user_id;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setUser_id(int user_id) {
//        this.user_id = user_id;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }

//    @Override
//    public String toString() {
//        return "AllUsers{" +
//                "user_id=" + user_id +
//                ", email='" + email + '\'' +
//                ", password='" + password + '\'' +
//                ", role='" + role + '\'' +
//                '}';
//    }
}
