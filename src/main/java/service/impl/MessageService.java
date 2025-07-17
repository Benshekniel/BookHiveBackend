package service.impl;

import model.entity.Message;
import model.entity.AllUsers;
import model.dto.MessageDto.*;
import model.repo.MessageRepository;
import model.repo.AllUsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final AllUsersRepo allUsersRepo;

    public MessageResponseDto sendMessage(MessageCreateDto createDto) {
        // User sender = allUsersRepo.findById((int) createDto.getSenderId().longValue())
        //         .orElseThrow(() -> new RuntimeException("Sender not found"));

        // User receiver = allUsersRepo.findById((int) createDto.getReceiverId().longValue())
        //         .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = new Message();
        message.setSenderId(createDto.getSenderId());
        message.setReceiverId(createDto.getReceiverId());
        message.setContent(createDto.getContent());
        message.setIsRead(false);

        Message savedMessage = messageRepository.save(message);
        return convertToResponseDto(savedMessage);
    }

    public List<MessageResponseDto> getConversation(Long user1Id, Long user2Id) {
        List<Message> messages = messageRepository.findConversationBetweenUsers(user1Id, user2Id);
        return messages.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<MessageResponseDto> getUserMessages(Long userId) {
        List<Message> messages = messageRepository.findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId);
        return messages.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setIsRead(true);
        messageRepository.save(message);
    }

    public void markConversationAsRead(Long receiverId, Long senderId) {
        List<Message> messages = messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAtAsc(senderId, receiverId);
        messages.forEach(message -> message.setIsRead(true));
        messageRepository.saveAll(messages);
    }

    public Long getUnreadMessageCount(Long userId) {
        return messageRepository.countByReceiverIdAndIsRead(userId, false);
    }

    public void deleteMessage(Long messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new RuntimeException("Message not found");
        }
        messageRepository.deleteById(messageId);
    }

    private MessageResponseDto convertToResponseDto(Message message) {
        MessageResponseDto dto = new MessageResponseDto();
        dto.setMessageId(message.getMessageId());
        dto.setSenderId(message.getSenderId());
        dto.setReceiverId(message.getReceiverId());
        
        // Fetch sender and receiver names
        // User sender = allUsersRepo.findById((int) message.getSenderId().longValue())
        //         .orElseThrow(() -> new RuntimeException("Sender not found"));
        // User receiver = allUsersRepo.findById((int) message.getReceiverId().longValue())
        //         .orElseThrow(() -> new RuntimeException("Receiver not found"));
        
        // dto.setSenderName(sender.getName());
        // dto.setReceiverName(receiver.getName());
        dto.setSenderName("Sender"); // Temporary placeholder
        dto.setReceiverName("Receiver"); // Temporary placeholder
        
        dto.setContent(message.getContent());
        dto.setIsRead(message.getIsRead());
        dto.setCreatedAt(message.getCreatedAt());
        return dto;
    }
}
