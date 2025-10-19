package model.dto.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DashboardStatsDTO {
    private Long pendingRequests;
    private Long booksReceived;
    private Long upcomingEvents;
    private Long totalDonations;
    
    private Integer pendingRequestsChange;
    private Integer booksReceivedChange;
    private Integer upcomingEventsChange;
    private Integer totalDonationsChange;
}