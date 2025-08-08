package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationProfileUpdateDTO {
    private String organizationName;
    private String contactPersonFirstName;
    private String contactPersonLastName;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String description;
    private String website;
}
