package service.Delivery.impl;

import model.entity.Message;
import model.entity.AllUsers;
import model.dto.Delivery.MessageDto.*;
import model.repo.Delivery.MessageRepository;
import model.repo.AllUsersRepo;
import model.repo.Delivery.AgentRepository;
import lombok.RequiredArgsConstructor;
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
            
            return convertToResponseDto(savedMessage);
        } catch (Exception e) {
            System.err.println("Error in sendMessage: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send message: " + e.getMessage());
        }
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

    public void markAsRead(Long messageId) {
        try {
            if (messageId == null) {
                throw new RuntimeException("Message ID cannot be null");
            }

            Message message = messageRepository.findById(messageId)
                    .orElseThrow(() -> new RuntimeException("Message not found with ID: " + messageId));
            message.setIsRead(true);
            messageRepository.save(message);
        } catch (Exception e) {
            System.err.println("Error in markAsRead: " + e.getMessage());
            throw new RuntimeException("Failed to mark message as read: " + e.getMessage());
        }
    }

    public void markConversationAsRead(Long receiverId, Long senderId) {
        try {
            if (receiverId == null || senderId == null) {
                throw new RuntimeException("Receiver ID and Sender ID cannot be null");
            }

            List<Message> messages = messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAtAsc(senderId, receiverId);
            messages.forEach(message -> message.setIsRead(true));
            messageRepository.saveAll(messages);
        } catch (Exception e) {
            System.err.println("Error in markConversationAsRead: " + e.getMessage());
            throw new RuntimeException("Failed to mark conversation as read: " + e.getMessage());
        }
    }

    public Long getUnreadMessageCount(Long userId) {
        try {
            if (userId == null) {
                throw new RuntimeException("User ID cannot be null");
            }

            return messageRepository.countByReceiverIdAndIsRead(userId, false);
        } catch (Exception e) {
            System.err.println("Error in getUnreadMessageCount: " + e.getMessage());
            return 0L; // Return 0 instead of throwing exception
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
                    sendMessage(createDto);
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

    public List<MessageResponseDto> getRecentMessages(Long userId, int limit) {
        try {
            if (userId == null) {
                throw new RuntimeException("User ID cannot be null");
            }

            List<Message> messages = messageRepository.findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId);
            return messages.stream()
                    .limit(Math.max(1, limit)) // Ensure limit is at least 1
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getRecentMessages: " + e.getMessage());
            throw new RuntimeException("Failed to get recent messages: " + e.getMessage());
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
            dto.setRead(message.getIsRead()); // Fixed: using setRead instead of setIsRead
            dto.setCreatedAt(message.getCreatedAt());
            dto.setUpdatedAt(message.getUpdatedAt());
            dto.setSentAt(message.getSentAt()); // Added missing sentAt field

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