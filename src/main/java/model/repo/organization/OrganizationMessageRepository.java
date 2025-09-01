package model.repo.organization;

import model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrganizationMessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationId(Long conversationId);
    List<Message> findByOrganizationId(Long orgId);
}
