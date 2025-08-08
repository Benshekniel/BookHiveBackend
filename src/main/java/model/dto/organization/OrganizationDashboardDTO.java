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
    
    public OrganizationDashboardDTO(Long orgId, String organizationName, String organizationType, DashboardStats stats) {
        this.orgId = orgId;
        this.organizationName = organizationName;
        this.organizationType = organizationType;
        this.stats = stats;
    }
    
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
        
        public DashboardStats(int pendingRequests, int booksReceived, int upcomingEvents, 
                            int totalDonations, int activeRequests, int deliveredDonations) {
            this.pendingRequests = pendingRequests;
            this.booksReceived = booksReceived;
            this.upcomingEvents = upcomingEvents;
            this.totalDonations = totalDonations;
            this.activeRequests = activeRequests;
            this.deliveredDonations = deliveredDonations;
        }
    }
}
