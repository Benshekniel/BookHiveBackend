package model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_requests")
public class BookRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private String status;

	@Column(nullable = false)
	private LocalDateTime requestedAt;

	@Column(nullable = false)
	private Long organizationId;

	public BookRequest() {}

	public BookRequest(String title, int quantity, String status, LocalDateTime requestedAt, Long organizationId) {
		this.title = title;
		this.quantity = quantity;
		this.status = status;
		this.requestedAt = requestedAt;
		this.organizationId = organizationId;
	}

	// Getters and setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public LocalDateTime getRequestedAt() { return requestedAt; }
	public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
	public Long getOrganizationId() { return organizationId; }
	public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
}
