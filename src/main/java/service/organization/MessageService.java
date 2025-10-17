package service.organization;

import model.dto.organization.MessageDTO;
import model.dto.organization.MessageCreateDTO;
import model.dto.organization.ConversationDTO;

import java.util.List;

public interface MessageService {

    List<ConversationDTO> getConversations(Long organizationId);

    List<MessageDTO> getConversation(Long organizationId, Long partnerId);

    MessageDTO sendMessage(MessageCreateDTO createDTO);

    void markConversationAsRead(Long organizationId, Long conversationId);
}