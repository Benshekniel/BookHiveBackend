package model.dto.BookStore;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

public class BSDonationDTO {

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DonationDetailsDTO {
        private Long id;
        private Long organizationId;

        private Long donorId;
        private String orgName;

        private String status; // PENDING, APPROVED, REJECTED, SHIPPED, IN_TRANSIT, RECEIVED

        private Integer quantityCurrent;
        private Integer quantity;

        private String notes;
        private String category;
    }}
