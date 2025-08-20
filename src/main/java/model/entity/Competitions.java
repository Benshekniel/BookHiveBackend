package model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;

@Entity
@Table(name = "competitions")
public class Competitions {

    @Id
    @Column(name = "competition_id", nullable = false)
    private String competitionId;

    @Column(name = "active_status", nullable = false)
    private boolean activeStatus;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "activated_at")
    private LocalDateTime activatedAt;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "entry_trust_score")
    private Integer entryTrustScore;

    @Column(name = "prize_trust_score", nullable = false)
    private Integer prizeTrustScore;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "voting_start_date_time")
    private LocalDateTime votingStartDateTime;

    @Column(name = "voting_end_date_time")
    private LocalDateTime votingEndDateTime;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "current_participants", nullable = false)
    private Integer currentParticipants;

    @Type(JsonBinaryType.class)
    @Column(name = "participant_emails", columnDefinition = "jsonb", nullable = true)
    private List<String> participantEmails;

    @Column(name = "theme")
    private String theme;

    @Type(JsonBinaryType.class)
    @Column(name = "rules", columnDefinition = "jsonb")
    private List<String> rules;

    @Type(JsonBinaryType.class)
    @Column(name = "judging_criteria", columnDefinition = "jsonb")
    private List<String> judgingCriteria;

    @Column(name = "banner_image")
    private String bannerImage;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Default constructor
    public Competitions() {
    }

    // Parameterized constructor
    public Competitions(String competitionId, boolean activeStatus, String createdBy, String title, Integer entryTrustScore, Integer prizeTrustScore,
                       LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime votingStartDateTime,
                       LocalDateTime votingEndDateTime, Integer maxParticipants, Integer currentParticipants,
                       List<String> participantEmails, String theme, List<String> rules, List<String> judgingCriteria,
                       String bannerImage, String description) {
        this.competitionId = competitionId;
        this.activeStatus = activeStatus;
        this.createdBy = createdBy;
        this.title = title;
        this.entryTrustScore = entryTrustScore;
        this.prizeTrustScore = prizeTrustScore;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.votingStartDateTime = votingStartDateTime;
        this.votingEndDateTime = votingEndDateTime;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.participantEmails = participantEmails;
        this.theme = theme;
        this.rules = rules;
        this.judgingCriteria = judgingCriteria;
        this.bannerImage = bannerImage;
        this.description = description;
    }

    // Getters and Setters
    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(LocalDateTime activatedAt) {
        this.activatedAt = activatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEntryTrustScore() {
        return entryTrustScore;
    }

    public void setEntryTrustScore(Integer entryTrustScore) {
        this.entryTrustScore = entryTrustScore;
    }

    public Integer getPrizeTrustScore() {
        return prizeTrustScore;
    }

    public void setPrizeTrustScore(Integer prizeTrustScore) {
        this.prizeTrustScore = prizeTrustScore;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getVotingStartDateTime() {
        return votingStartDateTime;
    }

    public void setVotingStartDateTime(LocalDateTime votingStartDateTime) {
        this.votingStartDateTime = votingStartDateTime;
    }

    public LocalDateTime getVotingEndDateTime() {
        return votingEndDateTime;
    }

    public void setVotingEndDateTime(LocalDateTime votingEndDateTime) {
        this.votingEndDateTime = votingEndDateTime;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public List<String> getParticipantEmails() {
        return participantEmails;
    }

    public void setParticipantEmails(List<String> participantEmails) {
        this.participantEmails = participantEmails;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    public List<String> getJudgingCriteria() {
        return judgingCriteria;
    }

    public void setJudgingCriteria(List<String> judgingCriteria) {
        this.judgingCriteria = judgingCriteria;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
