package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationProfileDTO {
    private Long orgId;
    private String organizationName;
    private String type;
    private String regNo;
    private String contactPersonFirstName;
    private String contactPersonLastName;
    private String email;
    private String phone;
    private Integer years;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String description;
    private String website;
    private String imageFileName;
    private String status;
}
