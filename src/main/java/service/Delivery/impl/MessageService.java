// service/Delivery/impl/MessageService.java
package service.Delivery.impl;

import model.entity.Message;
import model.entity.AllUsers;
import model.dto.Delivery.MessageDto.*;
import model.repo.AllUsersRepo;
import model.repo.Delivery.AgentRepository;
import lombok.RequiredArgsConstructor;
import model.repo.Delivery.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final AllUsersRepo allUsersRepo;
    private final AgentRepository agentRepository;
    private final SocketIOService socketIOService;

    public MessageResponseDto sendMessage(MessageCreateDto createDto) {
        try {
            System.out.println("Creating message - Sender: " + createDto.getSenderId() + ", Receiver: " + createDto.getReceiverId());

            // Validate input
            if (createDto.getSenderId() == null || createDto.getReceiverId() == null || createDto.getContent() == null) {
                throw new RuntimeException("Missing required fields: senderId, receiverId, or content");
            }

            // Create and save message
            Message message = new Message();
            message.setSenderId(createDto.getSenderId());
            message.setReceiverId(createDto.getReceiverId());
            message.setContent(createDto.getContent());
            message.setIsRead(false);

            Message savedMessage = messageRepository.save(message);
            System.out.println("Message saved with ID: " + savedMessage.getMessageId());

            MessageResponseDto response = convertToResponseDto(savedMessage);

            // Real-time notifications
            sendRealTimeNotifications(response);

            return response;
        } catch (Exception e) {
            System.err.println("Error in sendMessage: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send message: " + e.getMessage());
        }
    }

    private void sendRealTimeNotifications(MessageResponseDto message) {
        try {
            System.out.println("=== SENDING REAL-TIME NOTIFICATIONS ===");
            System.out.println("Message ID: " + message.getMessageId());
            System.out.println("Sender: " + message.getSenderId() + " (" + message.getSenderName() + ")");
            System.out.println("Receiver: " + message.getReceiverId() + " (" + message.getReceiverName() + ")");
            System.out.println("Content: " + message.getContent());

            // FIXED: Send properly structured message to receiver
            Map<String, Object> receiverPayload = new HashMap<>();
            receiverPayload.put("type", "new_message");
            receiverPayload.put("senderId", message.getSenderId());
            receiverPayload.put("receiverId", message.getReceiverId());

            // Create message object with consistent structure
            Map<String, Object> messageObj = new HashMap<>();
            messageObj.put("id", message.getMessageId());
            messageObj.put("messageId", message.getMessageId());
            messageObj.put("content", message.getContent());
            messageObj.put("message", message.getContent()); // fallback
            messageObj.put("senderName", message.getSenderName());
            messageObj.put("sender", message.getSenderName()); // fallback
            messageObj.put("receiverName", message.getReceiverName());
            messageObj.put("createdAt", message.getCreatedAt().toString());
            messageObj.put("timestamp", message.getCreatedAt().toString()); // fallback

            receiverPayload.put("message", messageObj);

            System.out.println("RECEIVER PAYLOAD: " + receiverPayload);
            socketIOService.sendMessageToUser(message.getReceiverId(), receiverPayload);

            // FIXED: Send properly structured confirmation to sender
            Map<String, Object> senderPayload = new HashMap<>();
            senderPayload.put("type", "message_sent");
            senderPayload.put("senderId", message.getSenderId());
            senderPayload.put("receiverId", message.getReceiverId());
            senderPayload.put("message", messageObj);

            System.out.println("SENDER PAYLOAD: " + senderPayload);
            socketIOService.sendMessageSentConfirmation(message.getSenderId(), senderPayload);

            // Update unread count for receiver
            Long unreadCount = getUnreadMessageCount(message.getReceiverId());
            System.out.println("UNREAD COUNT FOR RECEIVER " + message.getReceiverId() + ": " + unreadCount);
            socketIOService.sendUnreadCountToUser(message.getReceiverId(), unreadCount);

            // Send conversation update to both users
            sendConversationUpdateToUser(message.getSenderId(), message.getReceiverId());
            sendConversationUpdateToUser(message.getReceiverId(), message.getSenderId());

            System.out.println("=== REAL-TIME NOTIFICATIONS SENT ===");

        } catch (Exception e) {
            System.err.println("Error sending real-time notifications: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendConversationUpdateToUser(Long userId, Long partnerId) {
        try {
            List<Message> recentMessages = messageRepository.findConversationBetweenUsers(userId, partnerId);
            if (!recentMessages.isEmpty()) {
                Message lastMessage = recentMessages.get(recentMessages.size() - 1);

                // Get partner info
                AllUsers partner = allUsersRepo.findById(Math.toIntExact(partnerId)).orElse(null);

                Map<String, Object> conversationUpdate = Map.of(
                        "partnerId", partnerId,
                        "partnerName", partner != null ? partner.getName() : "User " + partnerId,
                        "lastMessage", lastMessage.getContent(),
                        "timestamp", lastMessage.getCreatedAt().toString(),
                        "unreadCount", countUnreadMessagesInConversation(partnerId, userId)
                );

                System.out.println("CONVERSATION UPDATE FOR USER " + userId + ": " + conversationUpdate);
                socketIOService.sendConversationUpdate(userId, conversationUpdate);
            }
        } catch (Exception e) {
            System.err.println("Error sending conversation update: " + e.getMessage());
        }
    }

    public void markAsRead(Long messageId) {
        try {
            if (messageId == null) {
                throw new RuntimeException("Message ID cannot be null");
            }

            Message message = messageRepository.findById(messageId)
                    .orElseThrow(() -> new RuntimeException("Message not found with ID: " + messageId));

            if (!message.getIsRead()) {
                message.setIsRead(true);
                messageRepository.save(message);

                // Send real-time update
                Long unreadCount = getUnreadMessageCount(message.getReceiverId());
                socketIOService.sendUnreadCountToUser(message.getReceiverId(), unreadCount);
                System.out.println("MARKED MESSAGE AS READ AND UPDATED UNREAD COUNT: " + unreadCount);
            }
        } catch (Exception e) {
            System.err.println("Error in markAsRead: " + e.getMessage());
            throw new RuntimeException("Failed to mark message as read: " + e.getMessage());
        }
    }

    // FIXED: Correct logic for marking conversation as read
    public void markConversationAsRead(Long receiverId, Long senderId) {
        try {
            System.out.println("=== MARKING CONVERSATION AS READ ===");
            System.out.println("Receiver: " + receiverId + ", Sender: " + senderId);

            if (receiverId == null || senderId == null) {
                throw new RuntimeException("Receiver ID and Sender ID cannot be null");
            }

            // FIXED: Mark messages where senderId sent TO receiverId (not the other way around)
            List<Message> messages = messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAtAsc(senderId, receiverId);
            System.out.println("Found " + messages.size() + " messages to mark as read");

            boolean hasUnreadMessages = messages.stream().anyMatch(msg -> !msg.getIsRead());

            if (hasUnreadMessages) {
                messages.forEach(message -> {
                    if (!message.getIsRead()) {
                        message.setIsRead(true);
                        System.out.println("Marking message " + message.getMessageId() + " as read");
                    }
                });
                messageRepository.saveAll(messages);

                // Send real-time update
                Long unreadCount = getUnreadMessageCount(receiverId);
                socketIOService.sendUnreadCountToUser(receiverId, unreadCount);
                System.out.println("Updated unread count after marking as read: " + unreadCount);
                System.out.println("=== CONVERSATION MARKED AS READ ===");
            } else {
                System.out.println("No unread messages found to mark as read");
            }
        } catch (Exception e) {
            System.err.println("Error in markConversationAsRead: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to mark conversation as read: " + e.getMessage());
        }
    }

    public void broadcastMessageToAgents(Long hubId, String messageContent, Long senderId) {
        try {
            if (hubId == null || messageContent == null || senderId == null) {
                throw new RuntimeException("Hub ID, message content, and sender ID cannot be null");
            }

            var agents = agentRepository.findByHubId(hubId);
            System.out.println("Broadcasting to " + agents.size() + " agents in hub " + hubId);

            if (agents.isEmpty()) {
                System.out.println("No agents found for hub " + hubId);
                return;
            }

            agents.forEach(agent -> {
                try {
                    MessageCreateDto createDto = new MessageCreateDto();
                    createDto.setSenderId(senderId);
                    createDto.setReceiverId(agent.getAgentId());
                    createDto.setContent(messageContent);
                    sendMessage(createDto); // This will automatically send real-time notifications
                    System.out.println("Message sent to agent: " + agent.getAgentId());
                } catch (Exception e) {
                    System.err.println("Failed to send message to agent " + agent.getAgentId() + ": " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("Error in broadcastMessageToAgents: " + e.getMessage());
            throw new RuntimeException("Failed to broadcast message to agents: " + e.getMessage());
        }
    }

    private long countUnreadMessagesInConversation(Long senderId, Long receiverId) {
        return messageRepository.countUnreadMessagesInConversation(senderId, receiverId);
    }

    public List<MessageResponseDto> getConversation(Long user1Id, Long user2Id) {
        try {
            System.out.println("Getting conversation between " + user1Id + " and " + user2Id);

            if (user1Id == null || user2Id == null) {
                throw new RuntimeException("User IDs cannot be null");
            }

            List<Message> messages = messageRepository.findConversationBetweenUsers(user1Id, user2Id);
            System.out.println("Found " + messages.size() + " messages in conversation");

            return messages.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getConversation: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get conversation: " + e.getMessage());
        }
    }

    public List<MessageResponseDto> getUserMessages(Long userId) {
        try {
            if (userId == null) {
                throw new RuntimeException("User ID cannot be null");
            }

            List<Message> messages = messageRepository.findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId);
            return messages.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getUserMessages: " + e.getMessage());
            throw new RuntimeException("Failed to get user messages: " + e.getMessage());
        }
    }

    public void deleteMessage(Long messageId) {
        try {
            if (messageId == null) {
                throw new RuntimeException("Message ID cannot be null");
            }

            if (!messageRepository.existsById(messageId)) {
                throw new RuntimeException("Message not found with ID: " + messageId);
            }
            messageRepository.deleteById(messageId);
        } catch (Exception e) {
            System.err.println("Error in deleteMessage: " + e.getMessage());
            throw new RuntimeException("Failed to delete message: " + e.getMessage());
        }
    }

    public List<MessageResponseDto> getHubConversations(Long hubManagerId) {
        try {
            if (hubManagerId == null) {
                throw new RuntimeException("Hub Manager ID cannot be null");
            }

            List<Message> messages = messageRepository.findBySenderIdOrReceiverIdOrderByCreatedAtDesc(hubManagerId, hubManagerId);
            return messages.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getHubConversations: " + e.getMessage());
            throw new RuntimeException("Failed to get hub conversations: " + e.getMessage());
        }
    }

    public Map<String, Object> getConversationSummary(Long hubManagerId) {
        try {
            if (hubManagerId == null) {
                throw new RuntimeException("Hub Manager ID cannot be null");
            }

            Map<String, Object> summary = new HashMap<>();
            List<Message> allMessages = messageRepository.findBySenderIdOrReceiverIdOrderByCreatedAtDesc(hubManagerId, hubManagerId);

            Map<Long, List<Message>> conversationGroups = allMessages.stream()
                    .collect(Collectors.groupingBy(message ->
                            message.getSenderId().equals(hubManagerId) ? message.getReceiverId() : message.getSenderId()
                    ));

            summary.put("totalConversations", conversationGroups.size());
            summary.put("totalMessages", allMessages.size());
            summary.put("unreadCount", getUnreadMessageCount(hubManagerId));

            return summary;
        } catch (Exception e) {
            System.err.println("Error in getConversationSummary: " + e.getMessage());
            throw new RuntimeException("Failed to get conversation summary: " + e.getMessage());
        }
    }

    public List<MessageResponseDto> getRecentMessages(Long userId, int limit) {
        try {
            if (userId == null) {
                throw new RuntimeException("User ID cannot be null");
            }

            List<Message> messages = messageRepository.findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId);
            return messages.stream()
                    .limit(Math.max(1, limit))
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getRecentMessages: " + e.getMessage());
            throw new RuntimeException("Failed to get recent messages: " + e.getMessage());
        }
    }

    public Long getUnreadMessageCount(Long userId) {
        try {
            if (userId == null) {
                throw new RuntimeException("User ID cannot be null");
            }

            Long count = messageRepository.countByReceiverIdAndIsRead(userId, false);
            System.out.println("UNREAD COUNT FOR USER " + userId + ": " + count);
            return count;
        } catch (Exception e) {
            System.err.println("Error in getUnreadMessageCount: " + e.getMessage());
            return 0L;
        }
    }

    private MessageResponseDto convertToResponseDto(Message message) {
        try {
            if (message == null) {
                throw new RuntimeException("Message cannot be null");
            }

            MessageResponseDto dto = new MessageResponseDto();
            dto.setMessageId(message.getMessageId());
            dto.setSenderId(message.getSenderId());
            dto.setReceiverId(message.getReceiverId());
            dto.setContent(message.getContent());
            dto.setRead(message.getIsRead());
            dto.setCreatedAt(message.getCreatedAt());
            dto.setUpdatedAt(message.getUpdatedAt());
            dto.setSentAt(message.getSentAt());

            // Safely fetch sender and receiver names
            try {
                if (message.getSenderId() != null) {
                    AllUsers sender = allUsersRepo.findById(Math.toIntExact(message.getSenderId()))
                            .orElse(null);
                    dto.setSenderName(sender != null ? sender.getName() : "User " + message.getSenderId());
                } else {
                    dto.setSenderName("Unknown Sender");
                }

                if (message.getReceiverId() != null) {
                    AllUsers receiver = allUsersRepo.findById(Math.toIntExact(message.getReceiverId()))
                            .orElse(null);
                    dto.setReceiverName(receiver != null ? receiver.getName() : "User " + message.getReceiverId());
                } else {
                    dto.setReceiverName("Unknown Receiver");
                }
            } catch (Exception e) {
                System.out.println("Warning: Could not fetch user names: " + e.getMessage());
                dto.setSenderName("User " + message.getSenderId());
                dto.setReceiverName("User " + message.getReceiverId());
            }

            return dto;
        } catch (Exception e) {
            System.err.println("Error in convertToResponseDto: " + e.getMessage());
            throw new RuntimeException("Failed to convert message to DTO: " + e.getMessage());
        }
    }
}