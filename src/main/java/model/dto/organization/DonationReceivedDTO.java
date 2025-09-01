package model.dto.organization;

import java.time.LocalDateTime;

public class DonationReceivedDTO {
	private Long donationId;
	private String donorName;
	private Double amount;
	private LocalDateTime receivedDate;
	private String notes;

	public DonationReceivedDTO() {}

	public Long getDonationId() { return donationId; }
	public void setDonationId(Long donationId) { this.donationId = donationId; }
	public String getDonorName() { return donorName; }
	public void setDonorName(String donorName) { this.donorName = donorName; }
	public Double getAmount() { return amount; }
	public void setAmount(Double amount) { this.amount = amount; }
	public LocalDateTime getReceivedDate() { return receivedDate; }
	public void setReceivedDate(LocalDateTime receivedDate) { this.receivedDate = receivedDate; }
	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }
}
