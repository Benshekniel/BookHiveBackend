package service.impl.hubmanager;

import model.entity.User;
import model.entity.agent.Agent;
import model.entity.delivery.Hub;
import model.entity.delivery.Delivery;
import model.entity.transaction.Transaction;
import model.entity.book.Book;
import model.dto.DeliveryCreateDto;
import model.dto.DeliveryResponseDto;
import model.dto.DeliveryStatsDto;
import model.repo.DeliveryRepository;
import model.repo.AgentRepository;
import model.repo.HubRepository;
import model.repo.TransactionRepository;
import model.repo.UserRepository;
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

    public DeliveryResponseDto createDelivery(DeliveryCreateDto createDto) {
        Transaction transaction = transactionRepository.findById(createDto.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Hub hub = hubRepository.findById(createDto.getHubId())
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        Agent agent = agentRepository.findById(createDto.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        Delivery delivery = new Delivery();
        delivery.setTransaction(transaction);
        delivery.setHub(hub);
        delivery.setAgent(agent);
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
        return deliveryRepository.findByHubHubId(hubId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<DeliveryResponseDto> getDeliveriesByAgent(Long agentId) {
        return deliveryRepository.findByAgentAgentId(agentId).stream()
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

        delivery.setAgent(agent);
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
        dto.setTransactionId(delivery.getTransaction().getTransactionId());
        dto.setHubId(delivery.getHub().getHubId());
        dto.setHubName(delivery.getHub().getName());
        dto.setAgentId(delivery.getAgent().getAgentId());
        dto.setAgentName(delivery.getAgent().getUser().getName());
        dto.setPickupAddress(delivery.getPickupAddress());
        dto.setDeliveryAddress(delivery.getDeliveryAddress());
        dto.setStatus(delivery.getStatus());
        dto.setPickupTime(delivery.getPickupTime());
        dto.setDeliveryTime(delivery.getDeliveryTime());
        dto.setTrackingNumber(delivery.getTrackingNumber());
        dto.setCreatedAt(delivery.getCreatedAt());

        // Book information
        Book book = delivery.getTransaction().getBook();
        dto.setBookTitle(book.getTitle());
        dto.setBookAuthor(book.getAuthor());

        // Customer information
        User borrower = delivery.getTransaction().getBorrower();
        dto.setCustomerName(borrower.getName());
        return dto;
    }
}