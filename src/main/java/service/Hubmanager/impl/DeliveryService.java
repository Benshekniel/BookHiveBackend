package service.Hubmanager.impl;

import model.entity.Agent;
import model.entity.Hub;
import model.entity.Delivery;
import model.entity.Transaction;
import model.entity.Book;
import model.dto.Hubmanager.DeliveryDto.*;
import model.repo.Hubmanager.DeliveryRepository;
import model.repo.Hubmanager.AgentRepository;
import model.repo.Hubmanager.HubRepository;
import model.repo.Hubmanager.TransactionRepository;
import model.repo.AllUsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AgentRepository agentRepository;
    private final HubRepository hubRepository;
    private final TransactionRepository transactionRepository;
    private final AllUsersRepo allUsersRepo;

    public DeliveryResponseDto createDelivery(DeliveryCreateDto createDto) {
        Transaction transaction = transactionRepository.findById(createDto.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Hub hub = hubRepository.findById(createDto.getHubId())
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        Agent agent = agentRepository.findById(createDto.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        Delivery delivery = new Delivery();
        delivery.setTransactionId(transaction.getTransactionId());
        delivery.setHubId(hub.getHubId());
        delivery.setAgentId(agent.getAgentId());
        delivery.setPickupAddress(createDto.getPickupAddress());
        delivery.setDeliveryAddress(createDto.getDeliveryAddress());
        delivery.setStatus(Delivery.DeliveryStatus.PENDING);
        delivery.setTrackingNumber(generateTrackingNumber());

        Delivery savedDelivery = deliveryRepository.save(delivery);
        return convertToResponseDto(savedDelivery);
    }

    public List<DeliveryResponseDto> getAllDeliveries() {
        List<Object[]> results = deliveryRepository.findAllDeliveriesWithAllDetails();

        return results.stream()
                .map(this::convertToResponseDtoWithAllDetails)
                .collect(Collectors.toList());
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

    public Optional<DeliveryResponseDto> getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .map(this::convertToResponseDto);
    }

    public Optional<DeliveryResponseDto> getDeliveryByTrackingNumber(String trackingNumber) {
        return deliveryRepository.findByTrackingNumber(trackingNumber)
                .map(this::convertToResponseDto);
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
        return convertToResponseDto(updatedDelivery);
    }

    public DeliveryResponseDto assignAgent(Long deliveryId, Long agentId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        delivery.setAgentId(agent.getAgentId());
        delivery.setStatus(Delivery.DeliveryStatus.ASSIGNED);

        Delivery updatedDelivery = deliveryRepository.save(delivery);
        return convertToResponseDto(updatedDelivery);
    }

    public List<DeliveryStatsDto> getDeliveryStats() {
        List<Object[]> stats = deliveryRepository.getDeliveryStatsByStatus();
        return stats.stream()
                .map(stat -> new DeliveryStatsDto(
                        (Delivery.DeliveryStatus) stat[0],
                        ((Number) stat[1]).longValue()
                ))
                .collect(Collectors.toList());
    }

    public List<DeliveryResponseDto> getTodaysDeliveries() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        return deliveryRepository.findByCreatedAtBetween(startOfDay, endOfDay).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteDelivery(Long deliveryId) {
        if (!deliveryRepository.existsById(deliveryId)) {
            throw new RuntimeException("Delivery not found");
        }
        deliveryRepository.deleteById(deliveryId);
    }

    private String generateTrackingNumber() {
        return "TRK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private DeliveryResponseDto convertToResponseDto(Delivery delivery) {
        DeliveryResponseDto dto = new DeliveryResponseDto();
        dto.setDeliveryId(delivery.getDeliveryId());
        dto.setTransactionId(delivery.getTransactionId());

        // Fetch Hub entity
        Hub hub = hubRepository.findById(delivery.getHubId())
                .orElseThrow(() -> new RuntimeException("Hub not found"));
        dto.setHubId(hub.getHubId());
        dto.setHubName(hub.getName());

        // Fetch Agent entity
        Agent agent = agentRepository.findById(delivery.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        dto.setAgentId(agent.getAgentId());

        dto.setAgentName("Agent"); // Placeholder

        dto.setPickupAddress(delivery.getPickupAddress());
        dto.setDeliveryAddress(delivery.getDeliveryAddress());
        dto.setStatus(delivery.getStatus());
        dto.setPickupTime(delivery.getPickupTime());
        dto.setDeliveryTime(delivery.getDeliveryTime());
        dto.setTrackingNumber(delivery.getTrackingNumber());
        dto.setCreatedAt(delivery.getCreatedAt());

        dto.setCustomerName("Customer"); // Placeholder
        return dto;
    }

    // Updated mapping to match the new query result indices (9 fields instead of 11)
    private DeliveryResponseDto convertToResponseDtoWithAllDetails(Object[] result) {
        DeliveryResponseDto dto = new DeliveryResponseDto();

        Delivery delivery = (Delivery) result[0];
        String hubName = result[1] != null ? result[1].toString() : "Unknown Hub";
        String agentName = result[2] != null ? result[2].toString() : "Unassigned";
        String agentEmail = result[3] != null ? result[3].toString() : "N/A";
        String agentPhone = result[4] != null ? result[4].toString() : "N/A"; // From Agent table
        Long transactionId = result[5] != null ? ((Number) result[5]).longValue() : null;
        String bookTitle = result[6] != null ? result[6].toString() : "Unknown Book";
        String customerName = result[7] != null ? result[7].toString() : "Unknown Customer";
        String customerEmail = result[8] != null ? result[8].toString() : "N/A";

        // Basic delivery info
        dto.setDeliveryId(delivery.getDeliveryId());
        dto.setTransactionId(delivery.getTransactionId());
        dto.setPickupAddress(delivery.getPickupAddress());
        dto.setDeliveryAddress(delivery.getDeliveryAddress());
        dto.setStatus(delivery.getStatus());
        dto.setPickupTime(delivery.getPickupTime());
        dto.setDeliveryTime(delivery.getDeliveryTime());
        dto.setTrackingNumber(delivery.getTrackingNumber());
        dto.setCreatedAt(delivery.getCreatedAt());

        // Hub details
        dto.setHubId(delivery.getHubId());
        dto.setHubName(hubName);

        // Agent details (phone from Agent table)
        dto.setAgentId(delivery.getAgentId());
        dto.setAgentName(agentName);
        dto.setAgentEmail(agentEmail);
        dto.setAgentPhone(agentPhone);

        // Book details
        dto.setBookTitle(bookTitle);
        dto.setBookAuthor("Unknown Author"); // Default since we can't fetch from DB

        // Customer details
        dto.setCustomerName(customerName);
        dto.setCustomerEmail(customerEmail);
        dto.setCustomerPhone("N/A"); // Default since we can't fetch from AllUsers

        // New fields from entity
        dto.setValue(delivery.getFormattedValue());
        dto.setPriority("normal");
        dto.setDescription("Book delivery: " + bookTitle);
        dto.setWeight(delivery.getWeight() != null ? delivery.getWeight() : "N/A");
        dto.setDimensions(delivery.getDimensions() != null ? delivery.getDimensions() : "N/A");
        dto.setPaymentMethod(delivery.getPaymentMethod() != null ? delivery.getPaymentMethod().name() : "N/A");
        dto.setDeliveryNotes(delivery.getDeliveryNotes() != null ? delivery.getDeliveryNotes() : "");
        dto.setEstimatedDelivery(delivery.getDeliveryTime());

        return dto;
    }
}