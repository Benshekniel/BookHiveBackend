package model.dto.organization;

import java.time.LocalDateTime;

public class BookRequestResponseDTO {
	private Long id;
	private String title;
	private int quantity;
	private String status;
	private LocalDateTime requestedAt;

	public BookRequestResponseDTO() {}

	public BookRequestResponseDTO(Long id, String title, int quantity, String status, LocalDateTime requestedAt) {
		this.id = id;
		this.title = title;
		this.quantity = quantity;
		this.status = status;
		this.requestedAt = requestedAt;
	}

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
}
