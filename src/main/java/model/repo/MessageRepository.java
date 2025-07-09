package model.repo;

import model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverUserIdOrderByCreatedAtDesc(Long receiverId);
    List<Message> findBySenderUserIdOrderByCreatedAtDesc(Long senderId);
    List<Message> findByReceiverUserIdAndSenderUserIdOrderByCreatedAtDesc(Long receiverId, Long senderId);
    List<Message> findByReceiverUserIdAndIsReadFalse(Long receiverId);
    long countByReceiverUserIdAndIsReadFalse(Long receiverId);
    
    // Additional methods needed by service
    @Query("SELECT m FROM Message m WHERE (m.sender.userId = :userId1 AND m.receiver.userId = :userId2) OR (m.sender.userId = :userId2 AND m.receiver.userId = :userId1) ORDER BY m.createdAt ASC")
    List<Message> findConversationBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    
    List<Message> findBySenderUserIdOrReceiverUserIdOrderByCreatedAtDesc(Long senderId, Long receiverId);
    List<Message> findBySenderUserIdAndReceiverUserIdOrderByCreatedAtAsc(Long senderId, Long receiverId);
    long countByReceiverUserIdAndIsRead(Long receiverId, boolean isRead);
}
