package model.dto.organization;

import java.time.LocalDateTime;

public class MessageDTO {
	private Long id;
	private Long senderId;
	private Long receiverId;
	private String content;
	private LocalDateTime sentAt;

	public MessageDTO() {}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getSenderId() { return senderId; }
	public void setSenderId(Long senderId) { this.senderId = senderId; }
	public Long getReceiverId() { return receiverId; }
	public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	public LocalDateTime getSentAt() { return sentAt; }
	public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}
