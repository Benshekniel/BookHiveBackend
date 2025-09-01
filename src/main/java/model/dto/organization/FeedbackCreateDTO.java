package model.dto.organization;

public class FeedbackCreateDTO {
	private Long organizationId;
	private String message;
	private int rating;

	public FeedbackCreateDTO() {}

	public Long getOrganizationId() { return organizationId; }
	public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	public int getRating() { return rating; }
	public void setRating(int rating) { this.rating = rating; }
}
