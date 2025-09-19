package service.Delivery.impl;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import model.dto.Delivery.DeliverySummaryDto;
import model.entity.*;
import model.dto.Delivery.DeliveryDto.*;
import model.repo.Delivery.*;
import model.repo.AllUsersRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AgentRepository agentRepository;
    private final HubRepository hubRepository;
    private final TransactionRepository transactionRepository;
    private final AllUsersRepo allUsersRepo;
    private final RouteAssignmentService routeAssignmentService; // Added
    private final RouteRepository routeRepository; // ADD THIS LINE

    public DeliveryResponseDto createDelivery(DeliveryCreateDto createDto) {
        log.info("Creating delivery for user: {} to address: {}", createDto.getUserId(), createDto.getDeliveryAddress());

        // Validate required data
        validateCreateDeliveryRequest(createDto);

        // Create delivery entity
        Delivery delivery = new Delivery();
        delivery.setTransactionId(createDto.getTransactionId());
        delivery.setUserId(createDto.getUserId());
        delivery.setPickupAddress(createDto.getPickupAddress());
        delivery.setDeliveryAddress(createDto.getDeliveryAddress());
        delivery.setStatus(Delivery.DeliveryStatus.PENDING);
        delivery.setTrackingNumber(generateTrackingNumber());
        delivery.setCreatedAt(LocalDateTime.now());

        // Automatic route assignment based on delivery address
        if (createDto.getHubId() != null) {
            Long routeId = routeAssignmentService.assignRouteByDeliveryAddress(
                    createDto.getDeliveryAddress(),
                    createDto.getHubId()
            );
            if (routeId != null) {
                delivery.setRouteId(routeId);
                log.info("Assigned route {} for delivery address: {}", routeId, createDto.getDeliveryAddress());
            } else {
                log.warn("Could not assign route for delivery address: {}", createDto.getDeliveryAddress());
            }

            // Automatic pickup route assignment based on pickup address
            Long prouteId = routeAssignmentService.assignPickupRouteByAddress(
                    createDto.getPickupAddress(),
                    createDto.getHubId()
            );
            if (prouteId != null) {
                delivery.setProuteId(prouteId);
                log.info("Assigned pickup route {} for pickup address: {}", prouteId, createDto.getPickupAddress());
            } else {
                log.warn("Could not assign pickup route for pickup address: {}", createDto.getPickupAddress());
            }
        }

        // Set optional fields
        if (createDto.getWeight() != null) {
            delivery.setWeight(createDto.getWeight());
        }
        if (createDto.getDimensions() != null) {
            delivery.setDimensions(createDto.getDimensions());
        }
        if (createDto.getDeliveryNotes() != null) {
            delivery.setDeliveryNotes(createDto.getDeliveryNotes());
        }

        Delivery savedDelivery = deliveryRepository.save(delivery);
        log.info("Created delivery with ID: {}, tracking: {}, route: {}, pickup route: {}",
                savedDelivery.getDeliveryId(),
                savedDelivery.getTrackingNumber(),
                savedDelivery.getRouteId(),
                savedDelivery.getProuteId());

        return convertToResponseDto(savedDelivery);
    }

    public List<DeliveryResponseDto> getAllDeliveries() {
        log.info("Fetching all deliveries with details");
        List<Object[]> results = deliveryRepository.findAllDeliveriesWithAllDetails();
        return results.stream()
                .map(this::convertToResponseDtoWithAllDetails)
                .collect(Collectors.toList());
    }

    public Optional<DeliveryResponseDto> getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .map(this::convertToResponseDto);
    }

    public Optional<DeliveryResponseDto> getDeliveryByTrackingNumber(String trackingNumber) {
        return deliveryRepository.findByTrackingNumber(trackingNumber)
                .map(this::convertToResponseDto);
    }

    public List<DeliveryResponseDto> getDeliveriesByHub(Long hubId) {
        return deliveryRepository.findByHubId(hubId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<DeliveryResponseDto> getDeliveriesByAgent(Long agentId) {
        return deliveryRepository.findByAgentId(agentId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<DeliveryResponseDto> getDeliveriesByStatus(Delivery.DeliveryStatus status) {
        return deliveryRepository.findByStatus(status).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<DeliveryResponseDto> getTodaysDeliveries() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        return deliveryRepository.findByCreatedAtBetween(startOfDay, endOfDay).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<DeliveryStatsDto> getDeliveryStats() {
        log.info("Fetching delivery statistics by status");
        List<Object[]> stats = deliveryRepository.getDeliveryStatsByStatus();
        return stats.stream()
                .map(stat -> {
                    DeliveryStatsDto dto = new DeliveryStatsDto();
                    dto.setStatus((Delivery.DeliveryStatus) stat[0]);
                    dto.setCount(((Number) stat[1]).longValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public DeliverySummaryDto getDeliverySummary() {
        log.info("Fetching comprehensive delivery summary");

        try {
            Object[] summaryData = deliveryRepository.getDeliverySummaryStats();

            DeliverySummaryDto summary = new DeliverySummaryDto();
            summary.setTotalDeliveries(((Number) summaryData[0]).intValue());
            summary.setActiveDeliveries(((Number) summaryData[1]).intValue());
            summary.setCompletedDeliveries(((Number) summaryData[2]).intValue());
            summary.setPendingDeliveries(((Number) summaryData[3]).intValue());
            summary.setFailedDeliveries(((Number) summaryData[4]).intValue());

            // Calculate success rate
            int total = summary.getTotalDeliveries();
            if (total > 0) {
                double successRate = (double) summary.getCompletedDeliveries() / total * 100;
                summary.setSuccessRate(successRate);
            } else {
                summary.setSuccessRate(0.0);
            }

            // Calculate average delivery time (mock for now)
            summary.setAvgDeliveryTime(24.5); // hours

            // Create status breakdown map
            Map<String, Integer> statusBreakdown = new HashMap<>();
            statusBreakdown.put("PENDING", summary.getPendingDeliveries());
            statusBreakdown.put("ACTIVE", summary.getActiveDeliveries());
            statusBreakdown.put("COMPLETED", summary.getCompletedDeliveries());
            statusBreakdown.put("FAILED", summary.getFailedDeliveries());
            summary.setStatusBreakdown(statusBreakdown);

            summary.setTimestamp(LocalDateTime.now());

            log.info("Delivery summary: {} total, {} active, {} completed",
                    summary.getTotalDeliveries(), summary.getActiveDeliveries(), summary.getCompletedDeliveries());

            return summary;

        } catch (Exception e) {
            log.error("Error fetching delivery summary: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch delivery summary", e);
        }
    }

    public Map<String, List<DeliveryResponseDto>> getBatchDeliveryDataByHubs(List<Long> hubIds) {
        log.info("Fetching batch delivery data for {} hubs", hubIds.size());

        Map<String, List<DeliveryResponseDto>> batchData = new HashMap<>();

        for (Long hubId : hubIds) {
            try {
                List<DeliveryResponseDto> hubDeliveries = getDeliveriesByHub(hubId);
                batchData.put(hubId.toString(), hubDeliveries);
                log.debug("Fetched {} deliveries for hub {}", hubDeliveries.size(), hubId);
            } catch (Exception e) {
                log.warn("Failed to fetch deliveries for hub {}: {}", hubId, e.getMessage());
                batchData.put(hubId.toString(), new ArrayList<>());
            }
        }

        return batchData;
    }

    public DeliveryResponseDto updateDeliveryStatus(Long deliveryId, Delivery.DeliveryStatus newStatus) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        delivery.setStatus(newStatus);

        // Set timestamps based on status
        switch (newStatus) {
            case PICKED_UP:
                delivery.setPickupTime(LocalDateTime.now());
                break;
            case DELIVERED:
                delivery.setDeliveryTime(LocalDateTime.now());
                break;
        }

        Delivery updatedDelivery = deliveryRepository.save(delivery);
        log.info("Updated delivery {} status to {}", deliveryId, newStatus);

        return convertToResponseDto(updatedDelivery);
    }

    public DeliveryResponseDto assignAgent(Long deliveryId, Long agentId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        delivery.setStatus(Delivery.DeliveryStatus.ASSIGNED);

        Delivery updatedDelivery = deliveryRepository.save(delivery);
        log.info("Assigned agent {} to delivery {}", agentId, deliveryId);

        return convertToResponseDto(updatedDelivery);
    }

    public void deleteDelivery(Long deliveryId) {
        if (!deliveryRepository.existsById(deliveryId)) {
            throw new RuntimeException("Delivery not found");
        }

        deliveryRepository.deleteById(deliveryId);
        log.info("Deleted delivery {}", deliveryId);
    }

    /**
     * Reassign routes for existing delivery
     */
    public DeliveryResponseDto reassignRoutes(Long deliveryId, Long hubId) {
        log.info("Reassigning routes for delivery: {} in hub: {}", deliveryId, hubId);

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        // Reassign delivery route
        Long newRouteId = routeAssignmentService.assignRouteByDeliveryAddress(
                delivery.getDeliveryAddress(), hubId);
        if (newRouteId != null) {
            delivery.setRouteId(newRouteId);
            log.info("Reassigned delivery route {} for delivery {}", newRouteId, deliveryId);
        }

        // Reassign pickup route
        Long newProuteId = routeAssignmentService.assignPickupRouteByAddress(
                delivery.getPickupAddress(), hubId);
        if (newProuteId != null) {
            delivery.setProuteId(newProuteId);
            log.info("Reassigned pickup route {} for delivery {}", newProuteId, deliveryId);
        }

        Delivery updatedDelivery = deliveryRepository.save(delivery);
        return convertToResponseDto(updatedDelivery);
    }

    // Helper methods
    private void validateCreateDeliveryRequest(DeliveryCreateDto createDto) {
        if (createDto.getPickupAddress() == null || createDto.getPickupAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Pickup address cannot be empty");
        }
        if (createDto.getDeliveryAddress() == null || createDto.getDeliveryAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery address cannot be empty");
        }
        if (createDto.getTransactionId() == null || createDto.getTransactionId() <= 0) {
            throw new IllegalArgumentException("Valid transaction ID is required");
        }
        if (createDto.getUserId() == null || createDto.getUserId() <= 0) {
            throw new IllegalArgumentException("Valid user ID is required");
        }
    }

    private String generateTrackingNumber() {
        return "TRK" + System.currentTimeMillis() + String.format("%03d", new Random().nextInt(1000));
    }

    private DeliveryResponseDto convertToResponseDto(Delivery delivery) {
        DeliveryResponseDto dto = new DeliveryResponseDto();

        // Basic delivery info
        dto.setDeliveryId(delivery.getDeliveryId());
        dto.setTransactionId(delivery.getTransactionId());
        dto.setUserId(delivery.getUserId());
        dto.setRouteId(delivery.getRouteId());
        dto.setProuteId(delivery.getProuteId());

        // Get hubId from route if route exists
        if (delivery.getRouteId() != null) {
            Optional<Route> routeOpt = routeRepository.findById(delivery.getRouteId());
            if (routeOpt.isPresent()) {
                dto.setHubId(routeOpt.get().getHubId());
            }
        }

        dto.setPickupAddress(delivery.getPickupAddress());
        dto.setDeliveryAddress(delivery.getDeliveryAddress());
        dto.setStatus(delivery.getStatus());
        dto.setPickupTime(delivery.getPickupTime());
        dto.setDeliveryTime(delivery.getDeliveryTime());
        dto.setTrackingNumber(delivery.getTrackingNumber());
        dto.setCreatedAt(delivery.getCreatedAt());

        // Additional fields
        dto.setWeight(delivery.getWeight() != null ? delivery.getWeight() : "N/A");
        dto.setDimensions(delivery.getDimensions() != null ? delivery.getDimensions() : "N/A");
        dto.setDeliveryNotes(delivery.getDeliveryNotes() != null ? delivery.getDeliveryNotes() : "");
        dto.setPaymentMethod(delivery.getPaymentMethod() != null ? delivery.getPaymentMethod().name() : "COD");

        // Set default values for missing related data
        dto.setAgentName("Unassigned");
        dto.setCustomerName("Customer");
        dto.setHubName("Hub");

        return dto;
    }    private DeliveryResponseDto convertToResponseDtoWithAllDetails(Object[] result) {
        DeliveryResponseDto dto = new DeliveryResponseDto();

        Delivery delivery = (Delivery) result[0];
        String hubName = result[1] != null ? result[1].toString() : "Unknown Hub";
        String agentName = result[2] != null ? result[2].toString() : "Unassigned";
        String agentEmail = result[3] != null ? result[3].toString() : "N/A";
        String agentPhone = result[4] != null ? result[4].toString() : "N/A";
        Long transactionId = result[5] != null ? ((Number) result[5]).longValue() : null;
        String bookTitle = result[6] != null ? result[6].toString() : "Unknown Book";
        String customerName = result[7] != null ? result[7].toString() : "Unknown Customer";
        String customerEmail = result[8] != null ? result[8].toString() : "N/A";

        // Basic delivery info
        dto.setDeliveryId(delivery.getDeliveryId());
        dto.setTransactionId(delivery.getTransactionId());
        dto.setUserId(delivery.getUserId());
        dto.setRouteId(delivery.getRouteId());
        dto.setPickupAddress(delivery.getPickupAddress());
        dto.setDeliveryAddress(delivery.getDeliveryAddress());
        dto.setStatus(delivery.getStatus());
        dto.setPickupTime(delivery.getPickupTime());
        dto.setDeliveryTime(delivery.getDeliveryTime());
        dto.setTrackingNumber(delivery.getTrackingNumber());
        dto.setCreatedAt(delivery.getCreatedAt());

        // Related entity details
        dto.setHubName(hubName);
        dto.setAgentName(agentName);
        dto.setAgentEmail(agentEmail);
        dto.setAgentPhone(agentPhone);
        dto.setBookTitle(bookTitle);
        dto.setCustomerName(customerName);
        dto.setCustomerEmail(customerEmail);

        // Additional fields
        dto.setWeight(delivery.getWeight() != null ? delivery.getWeight() : "N/A");
        dto.setDimensions(delivery.getDimensions() != null ? delivery.getDimensions() : "N/A");
        dto.setPaymentMethod(delivery.getPaymentMethod() != null ? delivery.getPaymentMethod().name() : "COD");
        dto.setDeliveryNotes(delivery.getDeliveryNotes() != null ? delivery.getDeliveryNotes() : "");

        return dto;
    }
}