package model.dto.organization;

public class BookRequestCreateDTO {
	private String title;
	private int quantity;
	private Long organizationId;

	public BookRequestCreateDTO() {}

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
	public Long getOrganizationId() { return organizationId; }
	public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
}
