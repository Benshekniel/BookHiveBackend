package model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubManagerResponseDto {
    private Long hubManagerId;
    private Long hubId;
    private String hubName;
    private Long userId;
    private String userName;
    private String userEmail;
    private LocalDateTime joinedAt;
}
