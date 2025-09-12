// Notification.java
package model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long organizationId;  // Can be null for system notifications
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, length = 1000)
    private String message;
    
    @Column(nullable = false)
    private String type;  // BOOK_REQUEST, DONATION, FEEDBACK, SYSTEM
    
    private boolean read;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private String action;  // URL or action to take when notification is clicked
    
    private Long referenceId;  // ID of the related entity (book request, donation, etc.)
    
    private String referenceType;  // Type of the reference (BOOK_REQUEST, DONATION, etc.)
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        read = false;
    }
}