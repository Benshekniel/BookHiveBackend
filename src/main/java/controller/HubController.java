package controller.hubmanager;

import service.impl.hubmanager.HubService;
import model.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hubs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HubController {

    private final HubService hubService;

    @PostMapping
    public ResponseEntity<HubResponseDto> createHub(@RequestBody HubCreateDto createDto) {
        try {
            HubResponseDto hub = hubService.createHub(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(hub);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<HubResponseDto>> getAllHubs() {
        List<HubResponseDto> hubs = hubService.getAllHubs();
        return ResponseEntity.ok(hubs);
    }

    @GetMapping("/{hubId}")
    public ResponseEntity<HubResponseDto> getHubById(@PathVariable Long hubId) {
        return hubService.getHubById(hubId)
                .map(hub -> ResponseEntity.ok(hub))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<HubResponseDto>> getHubsByCity(@PathVariable String city) {
        List<HubResponseDto> hubs = hubService.getHubsByCity(city);
        return ResponseEntity.ok(hubs);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<HubStatsDto>> getHubStats() {
        List<HubStatsDto> stats = hubService.getHubStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{hubId}/performance")
    public ResponseEntity<HubPerformanceDto> getHubPerformance(@PathVariable Long hubId) {
        try {
            HubPerformanceDto performance = hubService.getHubPerformance(hubId);
            return ResponseEntity.ok(performance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{hubId}/agents")
    public ResponseEntity<List<AgentResponseDto>> getHubAgents(@PathVariable Long hubId) {
        List<AgentResponseDto> agents = hubService.getHubAgents(hubId);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/{hubId}/deliveries")
    public ResponseEntity<List<DeliveryResponseDto>> getHubDeliveries(@PathVariable Long hubId) {
        List<DeliveryResponseDto> deliveries = hubService.getHubDeliveries(hubId);
        return ResponseEntity.ok(deliveries);
    }

    @PutMapping("/{hubId}")
    public ResponseEntity<HubResponseDto> updateHub(
            @PathVariable Long hubId,
            @RequestBody HubUpdateDto updateDto) {
        try {
            HubResponseDto updatedHub = hubService.updateHub(hubId, updateDto);
            return ResponseEntity.ok(updatedHub);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{hubId}/assign-manager")
    public ResponseEntity<HubManagerResponseDto> assignManager(
            @PathVariable Long hubId,
            @RequestBody AssignManagerDto assignDto) {
        try {
            HubManagerResponseDto hubManager = hubService.assignManager(hubId, assignDto.getUserId());
            return ResponseEntity.ok(hubManager);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{hubId}")
    public ResponseEntity<Void> deleteHub(@PathVariable Long hubId) {
        try {
            hubService.deleteHub(hubId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
