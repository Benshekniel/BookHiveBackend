package model.dto.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrganizationProfileDTO {
    private Long id;
    private String organizationName;
    private String registrationNumber;
    private String email;
    private String phone;
    private String address;
    private String description;
    private String website;
    private Integer established;
    private Integer studentCount;
    private String contactPerson;
    private String contactTitle;
    private String organizationType;
    private String profileImage;
    private Boolean publicProfile;
    private Boolean contactPermissions;
    private Boolean activityVisibility;
    private Boolean twoFactorEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}