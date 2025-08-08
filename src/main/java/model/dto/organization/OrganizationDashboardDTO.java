package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDashboardDTO {
    private Long orgId;
    private String organizationName;
    private String organizationType;
    private DashboardStats stats;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardStats {
        private int pendingRequests;
        private int booksReceived;
        private int upcomingEvents;
        private int totalDonations;
        private int activeRequests;
        private int deliveredDonations;
    }
}
