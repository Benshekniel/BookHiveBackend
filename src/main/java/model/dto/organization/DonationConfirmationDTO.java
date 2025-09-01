package model.dto.organization;

import java.time.LocalDateTime;

public class DonationConfirmationDTO {
	private Long donationId;
	private String confirmedBy;
	private LocalDateTime confirmationDate;
	private String notes;

	public DonationConfirmationDTO() {}

	public Long getDonationId() { return donationId; }
	public void setDonationId(Long donationId) { this.donationId = donationId; }
	public String getConfirmedBy() { return confirmedBy; }
	public void setConfirmedBy(String confirmedBy) { this.confirmedBy = confirmedBy; }
	public LocalDateTime getConfirmationDate() { return confirmationDate; }
	public void setConfirmationDate(LocalDateTime confirmationDate) { this.confirmationDate = confirmationDate; }
	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }
}
