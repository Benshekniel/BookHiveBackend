package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "agent_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "id_type", nullable = false)
    private String idType;

    @Column(name = "id_number", nullable = false)
    private String idNumber;

    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    @Column(name = "vehicle_registration", nullable = false)
    private String vehicleRegistration;

    @Column(name = "hub_id")
    private Long hubId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "documents_status")
    private DocumentStatus documentsStatus = DocumentStatus.PENDING;

    @Column(name = "id_front_url")
    private String idFrontUrl;

    @Column(name = "id_back_url")
    private String idBackUrl;

    @Column(name = "vehicle_rc_url")
    private String vehicleRcUrl;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "applied_date")
    private LocalDateTime appliedDate = LocalDateTime.now();

    @Column(name = "processed_date")
    private LocalDateTime processedDate;

    @Column(name = "processed_by")
    private Long processedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum ApplicationStatus {
        PENDING, APPROVED, REJECTED, UNDER_REVIEW
    }

    public enum DocumentStatus {
        PENDING, COMPLETE, INCOMPLETE
    }
}