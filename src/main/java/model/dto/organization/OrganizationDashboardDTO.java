package model.dto.organization;

public class OrganizationDashboardDTO {
	private int pendingRequests;
	private int booksReceived;
	private int upcomingEvents;
	private int totalDonations;

	public OrganizationDashboardDTO() {}

	public int getPendingRequests() { return pendingRequests; }
	public void setPendingRequests(int pendingRequests) { this.pendingRequests = pendingRequests; }
	public int getBooksReceived() { return booksReceived; }
	public void setBooksReceived(int booksReceived) { this.booksReceived = booksReceived; }
	public int getUpcomingEvents() { return upcomingEvents; }
	public void setUpcomingEvents(int upcomingEvents) { this.upcomingEvents = upcomingEvents; }
	public int getTotalDonations() { return totalDonations; }
	public void setTotalDonations(int totalDonations) { this.totalDonations = totalDonations; }
}
