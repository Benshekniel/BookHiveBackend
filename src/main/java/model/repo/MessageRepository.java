package model.repo;

import model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.receiverId = :receiverId AND m.receiverType = :receiverType " +
           "ORDER BY m.sentAt DESC")
    List<Message> findByReceiverIdAndReceiverTypeOrderBySentAtDesc(
            @Param("receiverId") Long receiverId, 
            @Param("receiverType") Message.ReceiverType receiverType);

    @Query("SELECT m FROM Message m WHERE m.senderId = :senderId AND m.senderType = :senderType " +
           "ORDER BY m.sentAt DESC")
    List<Message> findBySenderIdAndSenderTypeOrderBySentAtDesc(
            @Param("senderId") Long senderId, 
            @Param("senderType") Message.SenderType senderType);

    @Query("SELECT m FROM Message m WHERE m.messageId = :messageId AND m.receiverId = :receiverId AND m.receiverType = :receiverType")
    Message findByMessageIdAndReceiverIdAndReceiverType(
            @Param("messageId") Long messageId, 
            @Param("receiverId") Long receiverId, 
            @Param("receiverType") Message.ReceiverType receiverType);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiverId = :receiverId AND m.receiverType = :receiverType AND m.isRead = false")
    long countUnreadMessagesByReceiverIdAndReceiverType(
            @Param("receiverId") Long receiverId, 
            @Param("receiverType") Message.ReceiverType receiverType);
}
