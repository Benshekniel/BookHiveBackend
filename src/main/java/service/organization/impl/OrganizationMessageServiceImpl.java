package service.organization.impl;

import model.dto.organization.MessageCreateDTO;
import model.dto.organization.MessageDTO;
import org.springframework.stereotype.Service;
import service.organization.OrganizationMessageService;
import java.util.List;

@Service
public class OrganizationMessageServiceImpl implements OrganizationMessageService {

    @Override
    public List<?> getConversations(Long orgId) {
        // TODO: Implement get conversations for org
        return null;
    }

    @Override
    public List<MessageDTO> getMessages(Long conversationId) {
    // TODO: Map entities to DTOs
    return java.util.Collections.emptyList();
    }

    @Override
    public MessageDTO sendMessage(Long conversationId, MessageCreateDTO dto) {
        // TODO: Implement send message
        return null;
    }
}
