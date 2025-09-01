package controller.organization;

import model.dto.organization.MessageCreateDTO;
import model.dto.organization.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.organization.OrganizationMessageService;
import java.util.List;

@RestController
@RequestMapping("/api/organization-messages")
public class OrganizationMessageController {

    @Autowired
    private OrganizationMessageService messageService;

    @GetMapping("/conversations/{orgId}")
    public List<?> getConversations(@PathVariable Long orgId) {
        return messageService.getConversations(orgId);
    }

    @GetMapping("/messages/{conversationId}")
    public List<MessageDTO> getMessages(@PathVariable Long conversationId) {
        return messageService.getMessages(conversationId);
    }

    @PostMapping("/messages/{conversationId}/send")
    public MessageDTO sendMessage(@PathVariable Long conversationId, @RequestBody MessageCreateDTO dto) {
        return messageService.sendMessage(conversationId, dto);
    }
}
