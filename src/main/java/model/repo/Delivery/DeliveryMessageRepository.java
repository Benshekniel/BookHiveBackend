package model.repo.Delivery;

import model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryMessageRepository extends JpaRepository<Message, Long> {

    // Basic queries
    List<Message> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);
    List<Message> findBySenderIdOrderByCreatedAtDesc(Long senderId);
    List<Message> findByReceiverIdAndSenderIdOrderByCreatedAtDesc(Long receiverId, Long senderId);
    List<Message> findByReceiverIdAndIsReadFalse(Long receiverId);
    long countByReceiverIdAndIsReadFalse(Long receiverId);

    // Conversation queries
    @Query("SELECT m FROM Message m WHERE (m.senderId = :userId1 AND m.receiverId = :userId2) OR (m.senderId = :userId2 AND m.receiverId = :userId1) ORDER BY m.createdAt ASC")
    List<Message> findConversationBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    // User message queries
    @Query("SELECT m FROM Message m WHERE m.senderId = :userId OR m.receiverId = :userId ORDER BY m.createdAt DESC")
    List<Message> findBySenderIdOrReceiverIdOrderByCreatedAtDesc(@Param("userId") Long senderId, @Param("userId") Long receiverId);

    List<Message> findBySenderIdAndReceiverIdOrderByCreatedAtAsc(Long senderId, Long receiverId);

    // Count queries
    long countByReceiverIdAndIsRead(Long receiverId, boolean isRead);
    long countBySenderId(Long senderId);
    long countByReceiverId(Long receiverId);

    // Recent messages - replaced LIMIT with Spring Data's Pageable
    @Query("SELECT m FROM Message m WHERE m.senderId = :userId OR m.receiverId = :userId ORDER BY m.createdAt DESC")
    List<Message> findRecentMessagesByUser(@Param("userId") Long userId, org.springframework.data.domain.Pageable pageable);

    // Hub manager specific queries
    @Query("SELECT m FROM Message m WHERE m.senderId = :hubManagerId ORDER BY m.createdAt DESC")
    List<Message> findMessagesSentByHubManager(@Param("hubManagerId") Long hubManagerId);

    @Query("SELECT m FROM Message m WHERE m.receiverId = :hubManagerId ORDER BY m.createdAt DESC")
    List<Message> findMessagesReceivedByHubManager(@Param("hubManagerId") Long hubManagerId);

    // Conversation partners for hub manager
    @Query("SELECT DISTINCT " +
            "CASE WHEN m.senderId = :hubManagerId THEN m.receiverId ELSE m.senderId END " +
            "FROM Message m WHERE m.senderId = :hubManagerId OR m.receiverId = :hubManagerId")
    List<Long> findConversationPartnersByHubManager(@Param("hubManagerId") Long hubManagerId);

    // Last message in conversation - replaced LIMIT with Spring Data's Pageable
    @Query("SELECT m FROM Message m WHERE " +
            "((m.senderId = :userId1 AND m.receiverId = :userId2) OR (m.senderId = :userId2 AND m.receiverId = :userId1)) " +
            "ORDER BY m.createdAt DESC")
    List<Message> findLastMessageInConversation(@Param("userId1") Long userId1, @Param("userId2") Long userId2, org.springframework.data.domain.Pageable pageable);

    // Count unread messages in conversation
    @Query("SELECT COUNT(m) FROM Message m WHERE m.senderId = :senderId AND m.receiverId = :receiverId AND m.isRead = false")
    long countUnreadMessagesInConversation(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    // Delete conversation
    @Query("DELETE FROM Message m WHERE (m.senderId = :userId1 AND m.receiverId = :userId2) OR (m.senderId = :userId2 AND m.receiverId = :userId1)")
    void deleteConversationBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    // Messages by date range
    @Query("SELECT m FROM Message m WHERE m.createdAt BETWEEN :startDate AND :endDate ORDER BY m.createdAt DESC")
    List<Message> findMessagesByDateRange(@Param("startDate") java.time.LocalDateTime startDate,
                                          @Param("endDate") java.time.LocalDateTime endDate);

    // Search messages by content
    @Query("SELECT m FROM Message m WHERE m.content LIKE %:searchTerm% ORDER BY m.createdAt DESC")
    List<Message> findMessagesByContentContaining(@Param("searchTerm") String searchTerm);

    // Get all agents that have conversations with hub manager
    @Query("SELECT DISTINCT m.receiverId FROM Message m WHERE m.senderId = :hubManagerId " +
            "UNION " +
            "SELECT DISTINCT m.senderId FROM Message m WHERE m.receiverId = :hubManagerId AND m.senderId != :hubManagerId")
    List<Long> findAgentsInConversationWithHubManager(@Param("hubManagerId") Long hubManagerId);
}