package model.dto.organization;

public class OrganizationProfileUpdateDTO {
	private String organizationName;
	private String registrationNumber;
	private String email;
	private String phone;
	private String address;
	private String description;
	private String website;
	private String established;
	private String contactPerson;
	private String contactTitle;

	public OrganizationProfileUpdateDTO() {}

	// Getters and setters
	public String getOrganizationName() { return organizationName; }
	public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
	public String getRegistrationNumber() { return registrationNumber; }
	public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public String getWebsite() { return website; }
	public void setWebsite(String website) { this.website = website; }
	public String getEstablished() { return established; }
	public void setEstablished(String established) { this.established = established; }
	public String getContactPerson() { return contactPerson; }
	public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
	public String getContactTitle() { return contactTitle; }
	public void setContactTitle(String contactTitle) { this.contactTitle = contactTitle; }
}
