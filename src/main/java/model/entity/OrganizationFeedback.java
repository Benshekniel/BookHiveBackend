package model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "organization_feedback")
public class OrganizationFeedback {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long organizationId;

	@Column(nullable = false, length = 1000)
	private String message;

	@Column(nullable = false)
	private int rating;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	public OrganizationFeedback() {}

	public OrganizationFeedback(Long organizationId, String message, int rating, LocalDateTime createdAt) {
		this.organizationId = organizationId;
		this.message = message;
		this.rating = rating;
		this.createdAt = createdAt;
	}

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
