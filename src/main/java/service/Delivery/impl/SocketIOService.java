// src/main/java/service/Delivery/impl/SocketIOService.java
package service.Delivery.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SocketIOService {

    @Autowired
    private SocketIOServer server;

    // Store user sessions: userId -> sessionId (UUID)
    private Map<Long, UUID> userSessions = new ConcurrentHashMap<>();

    // Store session users: sessionId -> userId
    private Map<UUID, Long> sessionUsers = new ConcurrentHashMap<>();

    // Store active clients: sessionId -> SocketIOClient
    private Map<UUID, SocketIOClient> activeClients = new ConcurrentHashMap<>();

    @PostConstruct
    public void startServer() {
        try {
            server.addConnectListener(onConnected());
            server.addDisconnectListener(onDisconnected());
            server.addEventListener("join_user", JoinUserRequest.class, onUserJoined());

            // ADD MESSAGE HANDLERS:
            server.addEventListener("send_message", Map.class, onSendMessage());
            server.addEventListener("new_message", Map.class, onSendMessage());
            server.addEventListener("mobile_message", Map.class, onSendMessage());
            server.addEventListener("ping_test", Map.class, onPingTest());

            server.start();

            System.out.println("=================================================");
            System.out.println("Socket.IO server started successfully!");
            System.out.println("Host: " + server.getConfiguration().getHostname());
            System.out.println("Port: " + server.getConfiguration().getPort());
            System.out.println("URL: http://localhost:" + server.getConfiguration().getPort());
            System.out.println("✅ MESSAGE HANDLERS LOADED");
            System.out.println("=================================================");

            log.info("Socket.IO server started on port: " + server.getConfiguration().getPort());
        } catch (Exception e) {
            log.error("Failed to start Socket.IO server: " + e.getMessage(), e);
            throw new RuntimeException("Could not start Socket.IO server", e);
        }
    }

    @PreDestroy
    public void stopServer() {
        try {
            // Clean up all sessions and clients
            cleanupAllSessions();

            if (server != null) {
                server.stop();
                log.info("Socket.IO server stopped");
            }
        } catch (Exception e) {
            log.error("Error stopping Socket.IO server: " + e.getMessage(), e);
        }
    }

    private ConnectListener onConnected() {
        return client -> {
            try {
                UUID sessionId = client.getSessionId();
                activeClients.put(sessionId, client);
                log.info("Client connected: " + sessionId);
                System.out.println("NEW CLIENT CONNECTED: " + sessionId);
            } catch (Exception e) {
                log.error("Error in onConnected", e);
                // Disconnect client on error to prevent resource leaks
                safeDisconnectClient(client);
            }
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            try {
                UUID sessionId = client.getSessionId();

                // Clean up all references to this client
                cleanupClientSession(sessionId);

                log.info("Client disconnected: " + sessionId);
            } catch (Exception e) {
                log.error("Error in onDisconnected", e);
            }
        };
    }

    private DataListener<JoinUserRequest> onUserJoined() {
        return (client, data, ackSender) -> {
            try {
                Long userId = data.getUserId();
                UUID sessionId = client.getSessionId();

                log.info("User {} attempting to join", userId);
                System.out.println("USER JOINING: " + userId + " with session: " + sessionId);

                // Clean up old session if exists
                cleanupOldUserSession(userId);

                // Store new session
                userSessions.put(userId, sessionId);
                sessionUsers.put(sessionId, userId);
                activeClients.put(sessionId, client);

                // Join user to their personal room
                client.joinRoom("user_" + userId);

                log.info("User {} joined successfully with session {}", userId, sessionId);
                System.out.println("USER JOINED SUCCESSFULLY: " + userId);

                // Send confirmation using direct client (not room operations)
                safeClientSend(client, "user_joined", Map.of("status", "success", "userId", userId));

                if (ackSender != null) {
                    ackSender.sendAckData("joined");
                }
            } catch (Exception e) {
                log.error("Error in onUserJoined: " + e.getMessage(), e);
                if (ackSender != null) {
                    ackSender.sendAckData("error");
                }
                // Clean up on error
                safeDisconnectClient(client);
            }
        };
    }

    // NEW: Handle send_message events from mobile
    private DataListener<Map> onSendMessage() {
        return (client, data, ackSender) -> {
            try {
                System.out.println("📨 RECEIVED SEND_MESSAGE: " + data);
                log.info("Received send_message: " + data);

                Object senderIdObj = data.get("senderId");
                Object receiverIdObj = data.get("receiverId");
                String content = (String) data.get("content");
                String platform = (String) data.get("platform");

                if (senderIdObj == null || receiverIdObj == null || content == null) {
                    log.error("Missing required message fields");
                    System.err.println("❌ MISSING MESSAGE FIELDS: " + data);
                    return;
                }

                Long senderId = Long.valueOf(senderIdObj.toString());
                Long receiverId = Long.valueOf(receiverIdObj.toString());

                System.out.println("📤 MESSAGE: From " + senderId + " to " + receiverId + ": " + content);
                System.out.println("📱 Platform: " + platform);

                // Send message to receiver using existing method
                sendMessageToUser(receiverId, data);

                // Send confirmation to sender using existing method
                Map<String, Object> confirmation = Map.of(
                        "messageId", data.get("messageId"),
                        "status", "delivered",
                        "receiverId", receiverId
                );
                sendMessageSentConfirmation(senderId, confirmation);

                System.out.println("✅ MESSAGE PROCESSING COMPLETED");

            } catch (Exception e) {
                log.error("Error handling send_message: " + e.getMessage(), e);
                System.err.println("❌ ERROR IN SEND_MESSAGE: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    // NEW: Handle ping test events
    private DataListener<Map> onPingTest() {
        return (client, data, ackSender) -> {
            try {
                System.out.println("🏓 PING TEST RECEIVED: " + data);
                log.info("Ping test from client: " + client.getSessionId());

                Map<String, Object> pongData = Map.of(
                        "message", "Pong from Java server",
                        "timestamp", System.currentTimeMillis(),
                        "receivedData", data
                );

                safeClientSend(client, "pong_test", pongData);
                System.out.println("🏓 PONG SENT: " + pongData);

            } catch (Exception e) {
                log.error("Error handling ping_test: " + e.getMessage(), e);
            }
        };
    }

    // FIXED: Send message to specific user using direct client reference
    public void sendMessageToUser(Long userId, Object message) {
        try {
            UUID sessionId = userSessions.get(userId);
            if (sessionId != null) {
                SocketIOClient client = activeClients.get(sessionId);
                if (client != null && client.isChannelOpen()) {
                    System.out.println("SENDING MESSAGE TO USER " + userId + ": " + message);
                    safeClientSend(client, "new_message", message);
                    safeClientSend(client, "message_received", message); // Send both events for compatibility
                    log.info("Message sent to user: " + userId);
                    System.out.println("MESSAGE SENT TO USER: " + userId);
                } else {
                    // Clean up stale session
                    cleanupClientSession(sessionId);
                    log.warn("User {} client not available, session cleaned up", userId);
                    System.out.println("USER CLIENT NOT AVAILABLE: " + userId);
                }
            } else {
                log.warn("User {} is not online, message not sent", userId);
                System.out.println("USER NOT ONLINE: " + userId);
            }
        } catch (Exception e) {
            log.error("Error sending message to user " + userId + ": " + e.getMessage(), e);
        }
    }

    // FIXED: Send message sent confirmation
    public void sendMessageSentConfirmation(Long userId, Object message) {
        try {
            UUID sessionId = userSessions.get(userId);
            if (sessionId != null) {
                SocketIOClient client = activeClients.get(sessionId);
                if (client != null && client.isChannelOpen()) {
                    System.out.println("SENDING MESSAGE_SENT CONFIRMATION TO USER " + userId + ": " + message);
                    safeClientSend(client, "message_sent", message);
                    log.info("Message sent confirmation to user: " + userId);
                } else {
                    cleanupClientSession(sessionId);
                }
            }
        } catch (Exception e) {
            log.error("Error sending message sent confirmation to user " + userId + ": " + e.getMessage(), e);
        }
    }

    // FIXED: Send unread count update using direct client reference
    public void sendUnreadCountToUser(Long userId, Long unreadCount) {
        try {
            UUID sessionId = userSessions.get(userId);
            if (sessionId != null) {
                SocketIOClient client = activeClients.get(sessionId);
                if (client != null && client.isChannelOpen()) {
                    Map<String, Object> payload = Map.of("userId", userId, "unreadCount", unreadCount);
                    System.out.println("SENDING UNREAD COUNT TO USER " + userId + ": " + payload);
                    safeClientSend(client, "unread_count_update", payload);
                    log.info("Unread count sent to user {}: {}", userId, unreadCount);
                } else {
                    cleanupClientSession(sessionId);
                }
            }
        } catch (Exception e) {
            log.error("Error sending unread count to user " + userId + ": " + e.getMessage(), e);
        }
    }

    // FIXED: Send conversation update using direct client reference
    public void sendConversationUpdate(Long userId, Object conversationData) {
        try {
            UUID sessionId = userSessions.get(userId);
            if (sessionId != null) {
                SocketIOClient client = activeClients.get(sessionId);
                if (client != null && client.isChannelOpen()) {
                    System.out.println("SENDING CONVERSATION UPDATE TO USER " + userId + ": " + conversationData);
                    safeClientSend(client, "conversation_update", conversationData);
                    log.info("Conversation update sent to user: " + userId);
                } else {
                    cleanupClientSession(sessionId);
                }
            }
        } catch (Exception e) {
            log.error("Error sending conversation update to user " + userId + ": " + e.getMessage(), e);
        }
    }

    // NEW: Safe method to send events to clients
    private void safeClientSend(SocketIOClient client, String eventName, Object data) {
        try {
            if (client != null && client.isChannelOpen()) {
                client.sendEvent(eventName, data);
                System.out.println("SAFE CLIENT SEND - Event: " + eventName + ", Data: " + data);
            }
        } catch (Exception e) {
            log.error("Error sending event {} to client: {}", eventName, e.getMessage());
            // Disconnect client on send error to prevent further issues
            safeDisconnectClient(client);
        }
    }

    // NEW: Safe method to disconnect clients
    private void safeDisconnectClient(SocketIOClient client) {
        try {
            if (client != null && client.isChannelOpen()) {
                client.disconnect();
            }
        } catch (Exception e) {
            log.error("Error disconnecting client: {}", e.getMessage());
        }
    }

    // NEW: Clean up old user session
    private void cleanupOldUserSession(Long userId) {
        try {
            UUID oldSessionId = userSessions.get(userId);
            if (oldSessionId != null) {
                // Clean up old session
                cleanupClientSession(oldSessionId);
                log.info("Cleaned up old session for user {}", userId);
            }
        } catch (Exception e) {
            log.error("Error cleaning up old session for user {}: {}", userId, e.getMessage());
        }
    }

    // NEW: Clean up client session completely
    private void cleanupClientSession(UUID sessionId) {
        try {
            // Remove from all maps
            Long userId = sessionUsers.remove(sessionId);
            if (userId != null) {
                userSessions.remove(userId);
                log.info("User {} session cleaned up", userId);
                System.out.println("USER DISCONNECTED: " + userId);
            }

            // Remove client reference
            SocketIOClient client = activeClients.remove(sessionId);
            if (client != null) {
                // Leave all rooms to clean up room references
                try {
                    client.leaveRoom("user_" + userId);
                } catch (Exception e) {
                    log.warn("Error leaving room for user {}: {}", userId, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error cleaning up session {}: {}", sessionId, e.getMessage());
        }
    }

    // NEW: Clean up all sessions (for shutdown)
    private void cleanupAllSessions() {
        try {
            log.info("Cleaning up {} active sessions", activeClients.size());

            // Disconnect all clients
            activeClients.values().forEach(this::safeDisconnectClient);

            // Clear all maps
            activeClients.clear();
            userSessions.clear();
            sessionUsers.clear();

            log.info("All sessions cleaned up");
        } catch (Exception e) {
            log.error("Error during cleanup: {}", e.getMessage());
        }
    }

    // Check if user is online
    public boolean isUserOnline(Long userId) {
        UUID sessionId = userSessions.get(userId);
        if (sessionId != null) {
            SocketIOClient client = activeClients.get(sessionId);
            return client != null && client.isChannelOpen();
        }
        return false;
    }

    // Get online users count
    public int getOnlineUsersCount() {
        return (int) activeClients.values().stream()
                .filter(client -> client != null && client.isChannelOpen())
                .count();
    }

    // DTO for join user request
    public static class JoinUserRequest {
        private Long userId;

        public JoinUserRequest() {} // Default constructor

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "JoinUserRequest{userId=" + userId + "}";
        }
    }
}