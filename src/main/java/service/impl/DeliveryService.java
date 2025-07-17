package service.impl;

import model.entity.AllUsers;
import model.entity.Agent;
import model.entity.Hub;
import model.entity.Delivery;
import model.entity.Transaction;
import model.entity.Book;
import model.dto.DeliveryDto.*;
import model.repo.DeliveryRepository;
import model.repo.AgentRepository;
import model.repo.HubRepository;
import model.repo.TransactionRepository;
import model.repo.AllUsersRepo;
import model.repo.BookRepository;
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
    private final BookRepository bookRepository;

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

        // Fetch User for agent name
        // User agentUser = allUsersRepo.findById((int) agent.getUserId().longValue())
        //         .orElseThrow(() -> new RuntimeException("Agent user not found"));
        // dto.setAgentName(agentUser.getName());
        dto.setAgentName("Agent"); // Temporary placeholder

        dto.setPickupAddress(delivery.getPickupAddress());
        dto.setDeliveryAddress(delivery.getDeliveryAddress());
        dto.setStatus(delivery.getStatus());
        dto.setPickupTime(delivery.getPickupTime());
        dto.setDeliveryTime(delivery.getDeliveryTime());
        dto.setTrackingNumber(delivery.getTrackingNumber());
        dto.setCreatedAt(delivery.getCreatedAt());

        // Fetch Transaction entity
        Transaction transaction = transactionRepository.findById(delivery.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Fetch Book entity
        Book book = bookRepository.findById(transaction.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        dto.setBookTitle(book.getTitle());
        dto.setBookAuthor(book.getAuthor());

        // Fetch Customer entity
        // User borrower = allUsersRepo.findById((int) transaction.getBorrowerId().longValue())
        //         .orElseThrow(() -> new RuntimeException("Borrower not found"));
        // dto.setCustomerName(borrower.getName());
        dto.setCustomerName("Customer"); // Temporary placeholder
        return dto;
    }

    private DeliveryResponseDto convertToResponseDtoWithAllDetails(Object[] result) {
        Delivery delivery = (Delivery) result[0];
        String hubName = (String) result[1];
        String agentName = (String) result[2];
        String agentEmail = (String) result[3];
        String agentPhone = (String) result[4];
        // Handle transactionId - could be Long or String
        Long transactionId = null;
        if (result[5] != null) {
            if (result[5] instanceof Long) {
                transactionId = (Long) result[5];
            } else if (result[5] instanceof String) {
                try {
                    transactionId = Long.parseLong((String) result[5]);
                } catch (NumberFormatException e) {
                    transactionId = null;
                }
            }
        }
        String bookTitle = (String) result[6];
        String bookAuthor = (String) result[7];
        String customerName = (String) result[8];
        String customerEmail = (String) result[9];
        String customerPhone = (String) result[10];

        DeliveryResponseDto dto = new DeliveryResponseDto();

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
        dto.setHubName(hubName != null ? hubName : "Unknown Hub");

        // Agent details (from deliveries.agent_id = all_users.user_id)
        dto.setAgentId(delivery.getAgentId());
        dto.setAgentName(agentName != null ? agentName : "Unassigned");
        dto.setAgentEmail(agentEmail != null ? agentEmail : "N/A");
        dto.setAgentPhone(agentPhone != null ? agentPhone : "N/A");

        // Book details
        dto.setBookTitle(bookTitle != null ? bookTitle : "Unknown Book");
        dto.setBookAuthor(bookAuthor != null ? bookAuthor : "Unknown Author");

        // Customer details (from deliveries.user_id = all_users.user_id)
        dto.setCustomerName(customerName != null ? customerName : "Unknown Customer");
        dto.setCustomerEmail(customerEmail != null ? customerEmail : "N/A");
        dto.setCustomerPhone(customerPhone != null ? customerPhone : "N/A");

        // New fields from entity (removed priority)
        dto.setValue(delivery.getFormattedValue()); // Use helper method
        dto.setPriority("normal"); // Default value for frontend compatibility
        dto.setDescription("Book delivery: " + (bookTitle != null ? bookTitle : "Unknown Book"));
        dto.setWeight(delivery.getWeight() != null ? delivery.getWeight() : "N/A");
        dto.setDimensions(delivery.getDimensions() != null ? delivery.getDimensions() : "N/A");
        dto.setPaymentMethod(delivery.getPaymentMethod() != null ? delivery.getPaymentMethod().name() : "N/A");
        dto.setDeliveryNotes(delivery.getDeliveryNotes() != null ? delivery.getDeliveryNotes() : "");
        dto.setEstimatedDelivery(delivery.getDeliveryTime());

        return dto;
    }
}