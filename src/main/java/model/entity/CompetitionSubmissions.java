package model.entity;

import jakarta.persistence.*;
import java.util.Map;
import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;

@Entity
@Table(name = "competition_submissions")
public class CompetitionSubmissions {

    @Id
    @Column(name = "submission_id", nullable = false)
    private String submissionId;

    @Column(name = "competition_id", nullable = false)
    private String competitionId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "submission_name", nullable = false)
    private String submissionName;

    @Column(name = "vote_count", nullable = false)
    private Integer voteCount;

    @Type(JsonBinaryType.class)
    @Column(name = "votes", columnDefinition = "jsonb")
    private Map<String, Integer> votes;
    //email:score structure

    @Column(name = "flag", nullable = false)
    private Boolean flag;

    // Default constructor
    public CompetitionSubmissions() {
    }

    // Parameterized constructor
    public CompetitionSubmissions(String submissionId, String competitionId, String email, String userId,
                                 String submissionName, Integer voteCount, Map<String, Integer> votes, Boolean flag) {
        this.submissionId = submissionId;
        this.competitionId = competitionId;
        this.email = email;
        this.userId = userId;
        this.submissionName = submissionName;
        this.voteCount = voteCount;
        this.votes = votes;
        this.flag = flag;
    }

    // Getters and Setters
    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubmissionName() {
        return submissionName;
    }

    public void setSubmissionName(String submissionName) {
        this.submissionName = submissionName;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Map<String, Integer> getVotes() {
        return votes;
    }

    public void setVotes(Map<String, Integer> votes) {
        this.votes = votes;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}