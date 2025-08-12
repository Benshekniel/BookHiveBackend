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
    // Explicit getters and setters for all fields
    public Long getFeedbackId() { return feedbackId; }
    public void setFeedbackId(Long feedbackId) { this.feedbackId = feedbackId; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public FeedbackType getFeedbackType() { return feedbackType; }
    public void setFeedbackType(FeedbackType feedbackType) { this.feedbackType = feedbackType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public FeedbackCategory getCategory() { return category; }
    public void setCategory(FeedbackCategory category) { this.category = category; }

    public LocalDateTime getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(LocalDateTime submittedDate) { this.submittedDate = submittedDate; }

    public FeedbackStatus getStatus() { return status; }
    public void setStatus(FeedbackStatus status) { this.status = status; }

    public String getAdminResponse() { return adminResponse; }
    public void setAdminResponse(String adminResponse) { this.adminResponse = adminResponse; }

    public LocalDateTime getResponseDate() { return responseDate; }
    public void setResponseDate(LocalDateTime responseDate) { this.responseDate = responseDate; }

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
