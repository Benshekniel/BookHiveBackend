package service.organization;

import model.dto.organization.MessageCreateDTO;
import model.dto.organization.MessageDTO;
import java.util.List;

public interface OrganizationMessageService {
    List<?> getConversations(Long orgId);
    List<MessageDTO> getMessages(Long conversationId);
    MessageDTO sendMessage(Long conversationId, MessageCreateDTO dto);
}
