// Organization.java
package model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "organizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String email;
    
    private String phone;
    
    @Column(length = 500)
    private String address;
    
    @Column(length = 1000)
    private String description;
    
    private String imageUrl;
    
    private boolean twoFactorEnabled;
    
    private String websiteUrl;
    
    private String contactPerson;
    
    private String registrationNumber;
    
    private Long userId;  // Reference to AllUsers table
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Organization(String type, String reg_no, String fname, String lname,
            String email2, String encode, int phone2, Long yearsLong,
            String address2, String city, String state, String zip,
            String imageFileName, String fileType) {
        //TODO Auto-generated constructor stub
    }
}