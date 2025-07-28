package controller;

import model.dto.Hubmanager.MessageDto.*;
import service.Hubmanager.impl.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<MessageResponseDto> sendMessage(@RequestBody MessageCreateDto createDto) {
        try {
            System.out.println("Received message request: " + createDto.toString());
            MessageResponseDto response = messageService.sendMessage(createDto);
            System.out.println("Message sent successfully: " + response.getMessageId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/conversation/{user1Id}/{user2Id}")
    public ResponseEntity<List<MessageResponseDto>> getConversation(
            @PathVariable Long user1Id,
            @PathVariable Long user2Id) {
        try {
            System.out.println("Getting conversation between: " + user1Id + " and " + user2Id);
            List<MessageResponseDto> messages = messageService.getConversation(user1Id, user2Id);
            System.out.println("Found " + messages.size() + " messages");
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            System.err.println("Error getting conversation: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MessageResponseDto>> getUserMessages(@PathVariable Long userId) {
        try {
            List<MessageResponseDto> messages = messageService.getUserMessages(userId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            System.err.println("Error getting user messages: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/mark-read/{messageId}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long messageId) {
        try {
            messageService.markAsRead(messageId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error marking message as read: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/mark-conversation-read/{receiverId}/{senderId}")
    public ResponseEntity<Void> markConversationAsRead(
            @PathVariable Long receiverId,
            @PathVariable Long senderId) {
        try {
            messageService.markConversationAsRead(receiverId, senderId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error marking conversation as read: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<Map<String, Long>> getUnreadMessageCount(@PathVariable Long userId) {
        try {
            Long count = messageService.getUnreadMessageCount(userId);
            return ResponseEntity.ok(Map.of("unreadCount", count));
        } catch (Exception e) {
            System.err.println("Error getting unread count: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        try {
            messageService.deleteMessage(messageId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error deleting message: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Additional endpoints for hub manager specific functionality
    @GetMapping("/hub/{hubManagerId}/conversations")
    public ResponseEntity<List<MessageResponseDto>> getHubConversations(@PathVariable Long hubManagerId) {
        try {
            List<MessageResponseDto> messages = messageService.getHubConversations(hubManagerId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            System.err.println("Error getting hub conversations: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/hub/{hubManagerId}/summary")
    public ResponseEntity<Map<String, Object>> getConversationSummary(@PathVariable Long hubManagerId) {
        try {
            Map<String, Object> summary = messageService.getConversationSummary(hubManagerId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            System.err.println("Error getting conversation summary: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/hub/{hubId}/broadcast")
    public ResponseEntity<Map<String, String>> broadcastMessage(
            @PathVariable Long hubId,
            @RequestBody Map<String, Object> request) {
        try {
            System.out.println("Broadcasting message to hub: " + hubId);
            System.out.println("Request: " + request.toString());

            String message = (String) request.get("message");
            Long senderId = Long.valueOf(request.get("senderId").toString());

            messageService.broadcastMessageToAgents(hubId, message, senderId);
            return ResponseEntity.ok(Map.of("status", "Message broadcasted successfully"));
        } catch (Exception e) {
            System.err.println("Error broadcasting message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to broadcast message: " + e.getMessage()));
        }
    }

    @GetMapping("/recent/{userId}")
    public ResponseEntity<List<MessageResponseDto>> getRecentMessages(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<MessageResponseDto> messages = messageService.getRecentMessages(userId, limit);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            System.err.println("Error getting recent messages: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}