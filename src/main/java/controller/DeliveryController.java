package controller;

import model.entity.Delivery;
import model.dto.Hubmanager.DeliveryDto.*;
import model.dto.Hubmanager.AgentDto.AssignAgentDeliveryDto;
import service.Hubmanager.impl.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<DeliveryResponseDto> createDelivery(@RequestBody DeliveryCreateDto createDto) {
        try {
            DeliveryResponseDto delivery = deliveryService.createDelivery(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(delivery);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<DeliveryResponseDto>> getAllDeliveries() {
        List<DeliveryResponseDto> deliveries = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponseDto> getDeliveryById(@PathVariable Long deliveryId) {
        return deliveryService.getDeliveryById(deliveryId)
                .map(delivery -> ResponseEntity.ok(delivery))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<DeliveryResponseDto> getDeliveryByTrackingNumber(@PathVariable String trackingNumber) {
        return deliveryService.getDeliveryByTrackingNumber(trackingNumber)
                .map(delivery -> ResponseEntity.ok(delivery))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/hub/{hubId}")
    public ResponseEntity<List<DeliveryResponseDto>> getDeliveriesByHub(@PathVariable Long hubId) {
        List<DeliveryResponseDto> deliveries = deliveryService.getDeliveriesByHub(hubId);
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<DeliveryResponseDto>> getDeliveriesByAgent(@PathVariable Long agentId) {
        List<DeliveryResponseDto> deliveries = deliveryService.getDeliveriesByAgent(agentId);
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeliveryResponseDto>> getDeliveriesByStatus(@PathVariable String status) {
        try {
            Delivery.DeliveryStatus deliveryStatus = Delivery.DeliveryStatus.valueOf(status.toUpperCase());
            List<DeliveryResponseDto> deliveries = deliveryService.getDeliveriesByStatus(deliveryStatus);
            return ResponseEntity.ok(deliveries);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/today")
    public ResponseEntity<List<DeliveryResponseDto>> getTodaysDeliveries() {
        List<DeliveryResponseDto> deliveries = deliveryService.getTodaysDeliveries();
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<DeliveryStatsDto>> getDeliveryStats() {
        List<DeliveryStatsDto> stats = deliveryService.getDeliveryStats();
        return ResponseEntity.ok(stats);
    }

    @PutMapping("/{deliveryId}/status")
    public ResponseEntity<DeliveryResponseDto> updateDeliveryStatus(
            @PathVariable Long deliveryId,
            @RequestBody UpdateStatusDto statusDto) {
        try {
            Delivery.DeliveryStatus newStatus = statusDto.getStatus();
            DeliveryResponseDto updatedDelivery = deliveryService.updateDeliveryStatus(deliveryId, newStatus);
            return ResponseEntity.ok(updatedDelivery);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{deliveryId}/assign-agent")
    public ResponseEntity<DeliveryResponseDto> assignAgent(
            @PathVariable Long deliveryId,
            @RequestBody AssignAgentDeliveryDto assignDto) {
        try {
            DeliveryResponseDto updatedDelivery = deliveryService.assignAgent(deliveryId, assignDto.getAgentId());
            return ResponseEntity.ok(updatedDelivery);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long deliveryId) {
        try {
            deliveryService.deleteDelivery(deliveryId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}