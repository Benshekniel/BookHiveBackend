package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "organization_feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @Column(nullable = false)
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedbackType feedbackType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    private Integer rating; // 1-5 stars

    @Enumerated(EnumType.STRING)
    private FeedbackCategory category;

    @Column(nullable = false)
    private LocalDateTime submittedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedbackStatus status = FeedbackStatus.SUBMITTED;

    @Column(columnDefinition = "TEXT")
    private String adminResponse;

    private LocalDateTime responseDate;

    @PrePersist
    protected void onCreate() {
        submittedDate = LocalDateTime.now();
    }

    public enum FeedbackType {
        COMPLAINT, SUGGESTION, COMPLIMENT, GENERAL
    }

    public enum FeedbackCategory {
        SERVICE, PLATFORM, DONATION, REQUEST, OTHER
    }

    public enum FeedbackStatus {
        SUBMITTED, REVIEWED, RESOLVED, CLOSED
    }
}
