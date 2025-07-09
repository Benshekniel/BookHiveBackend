package controller.hubmanager;

import model.dto.*;
import model.entity.agent.Agent;
import service.impl.hubmanager.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AgentController {

    private final AgentService agentService;

    @PostMapping
    public ResponseEntity<AgentResponseDto> createAgent(@RequestBody AgentCreateDto createDto) {
        try {
            AgentResponseDto agent = agentService.createAgent(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(agent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AgentResponseDto>> getAllAgents() {
        List<AgentResponseDto> agents = agentService.getAllAgents();
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/{agentId}")
    public ResponseEntity<AgentResponseDto> getAgentById(@PathVariable Long agentId) {
        return agentService.getAgentById(agentId)
                .map(agent -> ResponseEntity.ok(agent))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<AgentResponseDto> getAgentByUserId(@PathVariable Long userId) {
        return agentService.getAgentByUserId(userId)
                .map(agent -> ResponseEntity.ok(agent))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/hub/{hubId}")
    public ResponseEntity<List<AgentResponseDto>> getAgentsByHub(@PathVariable Long hubId) {
        List<AgentResponseDto> agents = agentService.getAgentsByHub(hubId);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/available")
    public ResponseEntity<List<AgentResponseDto>> getAvailableAgents() {
        List<AgentResponseDto> agents = agentService.getAvailableAgents();
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/available/hub/{hubId}")
    public ResponseEntity<List<AgentResponseDto>> getAvailableAgentsByHub(@PathVariable Long hubId) {
        List<AgentResponseDto> agents = agentService.getAvailableAgentsByHub(hubId);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/performance/hub/{hubId}")
    public ResponseEntity<List<AgentPerformanceDto>> getAgentPerformanceByHub(@PathVariable Long hubId) {
        List<AgentPerformanceDto> performance = agentService.getAgentPerformanceByHub(hubId);
        return ResponseEntity.ok(performance);
    }

    @PutMapping("/{agentId}/status")
    public ResponseEntity<AgentResponseDto> updateAgentStatus(
            @PathVariable Long agentId,
            @RequestBody UpdateAgentStatusDto statusDto) {
        try {
            Agent.AvailabilityStatus status = Agent.AvailabilityStatus.valueOf(statusDto.getStatus().toUpperCase());
            AgentResponseDto updatedAgent = agentService.updateAgentStatus(agentId, status);
            return ResponseEntity.ok(updatedAgent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}