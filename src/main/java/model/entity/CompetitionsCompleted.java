package model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;

@Entity
@Table(name = "competitions_completed")
public class CompetitionsCompleted {

    @Id
    @Column(name = "competition_id", nullable = false)
    private String competitionId;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "ended_at", nullable = false)
    private LocalDateTime endedAt;

    @Type(JsonBinaryType.class)
    @Column(name = "leaderboard", columnDefinition = "jsonb")
    private Map<String, Integer> leaderboard; //( email : rank(int))

    @Type(JsonBinaryType.class)
    @Column(name = "submissions", columnDefinition = "jsonb")
    private Map<String, String> submissions; //(email : submissionName)

    // Default constructor
    public CompetitionsCompleted() {
    }

    // Parameterized constructor
    public CompetitionsCompleted(String competitionId, String createdBy, LocalDateTime createdAt, LocalDateTime endedAt,
                                Map<String, Integer> leaderboard, Map<String, String> submissions) {
        this.competitionId = competitionId;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.endedAt = endedAt;
        this.leaderboard = leaderboard;
        this.submissions = submissions;
    }

    // Getters and Setters
    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
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

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public Map<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Map<String, Integer> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public Map<String, String> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(Map<String, String> submissions) {
        this.submissions = submissions;
    }
}
