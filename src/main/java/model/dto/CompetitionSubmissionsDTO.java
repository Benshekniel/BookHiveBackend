package model.dto;

import java.util.Map;

public class CompetitionSubmissionsDTO {

    private String submissionId;
    private String competitionId;
    private String email;
    private String userId;
    private String title;
    private String content;
    private Integer voteCount;
    private Map<String, Integer> votes;
    private Boolean flag;

    public CompetitionSubmissionsDTO() {
    }

    public CompetitionSubmissionsDTO(String submissionId, String competitionId, String email, String userId, String title, String content) {
        this.submissionId = submissionId;
        this.competitionId = competitionId;
        this.email = email;
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
