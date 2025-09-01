package model.dto.organization;

import java.time.LocalDateTime;

public class FeedbackResponseDTO {
	private Long id;
	private Long organizationId;
	private String message;
	private int rating;
	private LocalDateTime createdAt;

	public FeedbackResponseDTO() {}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getOrganizationId() { return organizationId; }
	public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	public int getRating() { return rating; }
	public void setRating(int rating) { this.rating = rating; }
	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
