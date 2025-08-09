package controller.Delivery;

import model.entity.Delivery;
import model.dto.Delivery.DeliveryDto.*;
import model.dto.Delivery.AgentDto.AssignAgentDeliveryDto;
import service.Delivery.impl.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<?> createDelivery(@Valid @RequestBody DeliveryCreateDto createDto) {
        try {
            log.info("Creating delivery for address: {} to hub: {}", createDto.getDeliveryAddress(), createDto.getHubId());

            DeliveryResponseDto delivery = deliveryService.createDelivery(createDto);

            log.info("Successfully created delivery with ID: {} and tracking number: {}",
                    delivery.getDeliveryId(), delivery.getTrackingNumber());

            if (delivery.getRouteId() != null) {
                log.info("Delivery automatically assigned to route: {} ({})",
                        delivery.getRouteId(), delivery.getRouteName());
            } else {
                log.warn("Delivery created but no route assigned for address: {}", createDto.getDeliveryAddress());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(delivery);

        } catch (IllegalArgumentException e) {
            log.error("Invalid input for delivery creation: {}", e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid input: " + e.getMessage()));

        } catch (RuntimeException e) {
            log.error("Runtime error creating delivery: {}", e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to create delivery: " + e.getMessage()));

        } catch (Exception e) {
            log.error("Unexpected error creating delivery: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal server error occurred"));
        }
    }

    @GetMapping
    public ResponseEntity<List<DeliveryResponseDto>> getAllDeliveries() {
        try {
            List<DeliveryResponseDto> deliveries = deliveryService.getAllDeliveries();
            log.info("Retrieved {} deliveries", deliveries.size());
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            log.error("Error retrieving all deliveries: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<?> getDeliveryById(@PathVariable Long deliveryId) {
        try {
            return deliveryService.getDeliveryById(deliveryId)
                    .map(delivery -> ResponseEntity.ok(delivery))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error retrieving delivery {}: {}", deliveryId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving delivery"));
        }
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<?> getDeliveryByTrackingNumber(@PathVariable String trackingNumber) {
        try {
            return deliveryService.getDeliveryByTrackingNumber(trackingNumber)
                    .map(delivery -> ResponseEntity.ok(delivery))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error retrieving delivery by tracking number {}: {}", trackingNumber, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving delivery"));
        }
    }

    @GetMapping("/hub/{hubId}")
    public ResponseEntity<List<DeliveryResponseDto>> getDeliveriesByHub(@PathVariable Long hubId) {
        try {
            List<DeliveryResponseDto> deliveries = deliveryService.getDeliveriesByHub(hubId);
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            log.error("Error retrieving deliveries for hub {}: {}", hubId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<DeliveryResponseDto>> getDeliveriesByAgent(@PathVariable Long agentId) {
        try {
            List<DeliveryResponseDto> deliveries = deliveryService.getDeliveriesByAgent(agentId);
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            log.error("Error retrieving deliveries for agent {}: {}", agentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getDeliveriesByStatus(@PathVariable String status) {
        try {
            Delivery.DeliveryStatus deliveryStatus = Delivery.DeliveryStatus.valueOf(status.toUpperCase());
            List<DeliveryResponseDto> deliveries = deliveryService.getDeliveriesByStatus(deliveryStatus);
            return ResponseEntity.ok(deliveries);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid status: " + status));
        } catch (Exception e) {
            log.error("Error retrieving deliveries by status {}: {}", status, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving deliveries"));
        }
    }

    @GetMapping("/today")
    public ResponseEntity<List<DeliveryResponseDto>> getTodaysDeliveries() {
        try {
            List<DeliveryResponseDto> deliveries = deliveryService.getTodaysDeliveries();
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            log.error("Error retrieving today's deliveries: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<List<DeliveryStatsDto>> getDeliveryStats() {
        try {
            List<DeliveryStatsDto> stats = deliveryService.getDeliveryStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error retrieving delivery stats: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{deliveryId}/status")
    public ResponseEntity<?> updateDeliveryStatus(
            @PathVariable Long deliveryId,
            @Valid @RequestBody UpdateStatusDto statusDto) {
        try {
            Delivery.DeliveryStatus newStatus = statusDto.getStatus();
            DeliveryResponseDto updatedDelivery = deliveryService.updateDeliveryStatus(deliveryId, newStatus);
            return ResponseEntity.ok(updatedDelivery);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating delivery status for {}: {}", deliveryId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error updating delivery status"));
        }
    }

    @PutMapping("/{deliveryId}/assign-agent")
    public ResponseEntity<?> assignAgent(
            @PathVariable Long deliveryId,
            @Valid @RequestBody AssignAgentDeliveryDto assignDto) {
        try {
            DeliveryResponseDto updatedDelivery = deliveryService.assignAgent(deliveryId, assignDto.getAgentId());
            return ResponseEntity.ok(updatedDelivery);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error assigning agent to delivery {}: {}", deliveryId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error assigning agent"));
        }
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<?> deleteDelivery(@PathVariable Long deliveryId) {
        try {
            deliveryService.deleteDelivery(deliveryId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting delivery {}: {}", deliveryId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error deleting delivery"));
        }
    }

    // Helper method to create consistent error responses
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", true);
        error.put("message", message);
        error.put("timestamp", System.currentTimeMillis());
        return error;
    }
}