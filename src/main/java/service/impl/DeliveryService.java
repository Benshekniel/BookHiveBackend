package service.impl;

import model.entity.User;
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
import model.repo.UserRepository;
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
    private final UserRepository userRepository;
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
        return deliveryRepository.findAll().stream()
                .map(this::convertToResponseDto)
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
        User agentUser = userRepository.findById(agent.getUserId())
                .orElseThrow(() -> new RuntimeException("Agent user not found"));
        dto.setAgentName(agentUser.getName());
        
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
        User borrower = userRepository.findById(transaction.getBorrowerId())
                .orElseThrow(() -> new RuntimeException("Borrower not found"));
        dto.setCustomerName(borrower.getName());
        return dto;
    }
}