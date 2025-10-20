package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveCompetitionDTO {
    private String competitionId;
    private Boolean activeStatus;
    private Boolean pauseStatus;
    private String title;
    private String theme;
    private String description;
    private Integer prizeTrustScore;
    private Integer entryTrustScore;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime votingStartDateTime;
    private LocalDateTime votingEndDateTime;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private String bannerImage;
    private LocalDateTime createdAt;
    private LocalDateTime activatedAt;
}
