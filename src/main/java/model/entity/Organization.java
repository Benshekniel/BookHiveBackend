package model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "organizations")
public class Organization {
    
    @Id
    @Column(nullable = true)
    private Long id;

    @Column(nullable = true)
    private String regNo;

    @Column(nullable = false)
    private String organizationName;

    @Column
    private String registrationNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String website;

    @Column
    private Integer established;

    @Column
    private Integer studentCount;

    @Column
    private String contactPerson;

    @Column
    private String contactTitle;

    @Column
    private String organizationType = "school";

    @Column
    private String profileImage;

    @Column(nullable = false)
    private Boolean publicProfile = true;

    @Column(nullable = false)
    private Boolean contactPermissions = true;

    @Column(nullable = false)
    private Boolean activityVisibility = true;

    @Column(nullable = false)
    private Boolean twoFactorEnabled = false;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookRequest> bookRequests = new HashSet<>();

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Donation> donations = new HashSet<>();

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Feedback> feedbacks = new HashSet<>();

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Organization(String type, String trim, String trim2, String trim3, String email2, String encode,
            String trim4, int years, Object object, Object object2, Object object3, Object object4,
            String imageFileName, String fileType) {
        //TODO Auto-generated constructor stub
    }
}