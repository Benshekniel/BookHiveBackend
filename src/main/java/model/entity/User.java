package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // Ensures DB column is user_id
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    // Temporarily allow nulls for schema migration
    @Column(name = "email", nullable = true, unique = true)
    private String email;

    @Column(name = "password", nullable = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum UserRole {
        USER,
        ADMIN,
        HUB_MANAGER,
        AGENT,
        MODERATOR,
        DELIVERY_AGENT
    }
}
