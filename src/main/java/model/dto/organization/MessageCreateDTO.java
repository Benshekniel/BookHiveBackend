package model.dto.organization;

public class MessageCreateDTO {
	private Long senderId;
	private Long receiverId;
	private String content;

	public MessageCreateDTO() {}

	public Long getSenderId() { return senderId; }
	public void setSenderId(Long senderId) { this.senderId = senderId; }
	public Long getReceiverId() { return receiverId; }
	public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
}
