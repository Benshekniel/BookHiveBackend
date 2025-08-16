package model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CompetitionDTO {

    private String competitionId;

    private String title;
    private Integer entryTrustScore;
    private Integer prizeTrustScore;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime votingStartDateTime;
    private LocalDateTime votingEndDateTime;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private String theme;
    private List<String> rules;
    private List<String> judgingCriteria;
    private String bannerImage;
    private String description;
    private boolean activeStatus;
    private String createdBy;

    //Without compId,bannerImage
    public CompetitionDTO(String title, Integer entryTrustScore, Integer prizeTrustScore, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime votingStartDateTime, LocalDateTime votingEndDateTime, Integer maxParticipants, String theme, List<String> rules, List<String> judgingCriteria, String description) {
        this.title = title;
        this.entryTrustScore = entryTrustScore;
        this.prizeTrustScore = prizeTrustScore;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.votingStartDateTime = votingStartDateTime;
        this.votingEndDateTime = votingEndDateTime;
        this.maxParticipants = maxParticipants;
        this.theme = theme;
        this.rules = rules;
        this.judgingCriteria = judgingCriteria;
        this.description = description;
    }



    public CompetitionDTO() {
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getEntryTrustScore() {
        return entryTrustScore;
    }

    public Integer getPrizeTrustScore() {
        return prizeTrustScore;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public LocalDateTime getVotingStartDateTime() {
        return votingStartDateTime;
    }

    public LocalDateTime getVotingEndDateTime() {
        return votingEndDateTime;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public String getTheme() {
        return theme;
    }

    public List<String> getRules() {
        return rules;
    }

    public List<String> getJudgingCriteria() {
        return judgingCriteria;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEntryTrustScore(Integer entryTrustScore) {
        this.entryTrustScore = entryTrustScore;
    }

    public void setPrizeTrustScore(Integer prizeTrustScore) {
        this.prizeTrustScore = prizeTrustScore;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setVotingStartDateTime(LocalDateTime votingStartDateTime) {
        this.votingStartDateTime = votingStartDateTime;
    }

    public void setVotingEndDateTime(LocalDateTime votingEndDateTime) {
        this.votingEndDateTime = votingEndDateTime;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    public void setJudgingCriteria(List<String> judgingCriteria) {
        this.judgingCriteria = judgingCriteria;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "CompetitionDTO{" +
                "competitionId='" + competitionId + '\'' +
                ", title='" + title + '\'' +
                ", entryTrustScore=" + entryTrustScore +
                ", prizeTrustScore=" + prizeTrustScore +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", votingStartDateTime=" + votingStartDateTime +
                ", votingEndDateTime=" + votingEndDateTime +
                ", maxParticipants=" + maxParticipants +
                ", currentParticipants=" + currentParticipants +
                ", theme='" + theme + '\'' +
                ", rules=" + rules +
                ", judgingCriteria=" + judgingCriteria +
                ", bannerImage='" + bannerImage + '\'' +
                ", description='" + description + '\'' +
                ", activeStatus=" + activeStatus +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
