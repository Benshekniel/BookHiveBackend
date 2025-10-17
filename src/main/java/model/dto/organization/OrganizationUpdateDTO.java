package model.dto.organization;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrganizationUpdateDTO {
    @NotBlank(message = "Organization name is required")
    private String organizationName;
    
    private String registrationNumber;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Phone number is required")
    private String phone;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    private String description;
    private String website;
    private Integer established;
    private Integer studentCount;
    
    @NotBlank(message = "Contact person is required")
    private String contactPerson;
    
    private String contactTitle;
    private String organizationType;
    private Boolean publicProfile = true;
    private Boolean contactPermissions = true;
    private Boolean activityVisibility = true;
}