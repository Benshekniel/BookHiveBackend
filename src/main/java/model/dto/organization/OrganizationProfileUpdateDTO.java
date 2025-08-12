package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationProfileUpdateDTO {
    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getContactPersonFirstName() { return contactPersonFirstName; }
    public void setContactPersonFirstName(String contactPersonFirstName) { this.contactPersonFirstName = contactPersonFirstName; }

    public String getContactPersonLastName() { return contactPersonLastName; }
    public void setContactPersonLastName(String contactPersonLastName) { this.contactPersonLastName = contactPersonLastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
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
