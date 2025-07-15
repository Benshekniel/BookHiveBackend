package service.impl;
import model.entity.User;
import model.entity.HubManager;
import model.entity.Hub;
import model.entity.Delivery;
import model.entity.Agent;
import model.dto.HubDto.*;
import model.dto.AgentDto.AgentResponseDto;
import model.dto.DeliveryDto.DeliveryResponseDto;
import model.repo.HubRepository;
import model.repo.AgentRepository;
import model.repo.DeliveryRepository;
import model.repo.UserRepository;
import model.repo.HubManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HubService {

    private final HubRepository hubRepository;
    private final AgentRepository agentRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final HubManagerRepository hubManagerRepository;

    public HubResponseDto createHub(HubCreateDto createDto) {
        Hub hub = new Hub();
        hub.setName(createDto.getName());
        hub.setAddress(createDto.getAddress());
        hub.setCity(createDto.getCity());
        hub.setNumberOfRoutes(0);

        Hub savedHub = hubRepository.save(hub);
        return convertToResponseDto(savedHub);
    }

    public List<HubResponseDto> getAllHubs() {
        return hubRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<HubResponseDto> getHubById(Long hubId) {
        return hubRepository.findById(hubId)
                .map(this::convertToResponseDto);
    }

    public List<HubResponseDto> getHubsByCity(String city) {
        return hubRepository.findByCity(city).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public HubResponseDto updateHub(Long hubId, HubUpdateDto updateDto) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        if (updateDto.getName() != null) {
            hub.setName(updateDto.getName());
        }
        if (updateDto.getAddress() != null) {
            hub.setAddress(updateDto.getAddress());
        }
        if (updateDto.getCity() != null) {
            hub.setCity(updateDto.getCity());
        }
        if (updateDto.getNumberOfRoutes() != null) {
            hub.setNumberOfRoutes(updateDto.getNumberOfRoutes());
        }

        Hub updatedHub = hubRepository.save(hub);
        return convertToResponseDto(updatedHub);
    }

    public HubManagerResponseDto assignManager(Long hubId, Long userId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is already a hub manager
        if (hubManagerRepository.existsByUserId(userId)) {
            throw new RuntimeException("User is already assigned as a hub manager");
        }

        HubManager hubManager = new HubManager();
        hubManager.setHubId(hub.getHubId());
        hubManager.setUserId(user.getUserId());

        // Update user role
        user.setRole(User.UserRole.HUB_MANAGER);
        userRepository.save(user);

        // Update hub manager ID
        hub.setHubManagerId(userId);
        hubRepository.save(hub);

        HubManager savedHubManager = hubManagerRepository.save(hubManager);
        return convertToHubManagerResponseDto(savedHubManager);
    }

    public List<HubStatsDto> getHubStats() {
        List<Hub> hubs = hubRepository.findAll();
        return hubs.stream()
                .map(this::convertToStatsDto)
                .collect(Collectors.toList());
    }

    public HubPerformanceDto getHubPerformance(Long hubId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        return calculateHubPerformance(hub);
    }

    public List<AgentResponseDto> getHubAgents(Long hubId) {
        return agentRepository.findByHubId(hubId).stream()
                .map(this::convertAgentToResponseDto)
                .collect(Collectors.toList());
    }

    public List<DeliveryResponseDto> getHubDeliveries(Long hubId) {
        return deliveryRepository.findByHubId(hubId).stream()
                .map(this::convertDeliveryToResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteHub(Long hubId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        // Check if hub has active deliveries
        long activeDeliveries = deliveryRepository.countByHubIdAndStatusIn(
                hubId,
                List.of(Delivery.DeliveryStatus.ASSIGNED, Delivery.DeliveryStatus.PICKED_UP, Delivery.DeliveryStatus.IN_TRANSIT)
        );

        if (activeDeliveries > 0) {
            throw new RuntimeException("Cannot delete hub with active deliveries");
        }

        // Remove hub manager role from users
        List<HubManager> hubManagers = hubManagerRepository.findAllByHubId(hubId);
        for (HubManager hubManager : hubManagers) {
            User user = userRepository.findById(hubManager.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setRole(User.UserRole.USER);
            userRepository.save(user);
        }

        hubRepository.deleteById(hubId);
    }

    private HubResponseDto convertToResponseDto(Hub hub) {
        HubResponseDto dto = new HubResponseDto();
        dto.setHubId(hub.getHubId());
        dto.setName(hub.getName());
        dto.setAddress(hub.getAddress());
        dto.setCity(hub.getCity());
        dto.setNumberOfRoutes(hub.getNumberOfRoutes());
        dto.setCreatedAt(hub.getCreatedAt());
        dto.setHubManagerId(hub.getHubManagerId());

        // Get hub manager name if exists
        if (hub.getHubManagerId() != null) {
            userRepository.findById(hub.getHubManagerId())
                    .ifPresent(user -> dto.setHubManagerName(user.getName()));
        }

        // Get counts
        dto.setTotalAgents(agentRepository.countByHubId(hub.getHubId()));
        dto.setActiveAgents(agentRepository.countByHubIdAndAvailabilityStatus(
                hub.getHubId(), Agent.AvailabilityStatus.AVAILABLE));
        dto.setTotalDeliveries(deliveryRepository.countByHubId(hub.getHubId()));

        return dto;
    }

    private HubManagerResponseDto convertToHubManagerResponseDto(HubManager hubManager) {
        HubManagerResponseDto dto = new HubManagerResponseDto();
        dto.setHubManagerId(hubManager.getHubManagerId());
        
        // Fetch Hub entity
        Hub hub = hubRepository.findById(hubManager.getHubId())
                .orElseThrow(() -> new RuntimeException("Hub not found"));
        dto.setHubId(hub.getHubId());
        dto.setHubName(hub.getName());
        
        // Fetch User entity
        User user = userRepository.findById(hubManager.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getName());
        dto.setUserEmail(user.getEmail());
        
        dto.setJoinedAt(hubManager.getJoinedAt());
        return dto;
    }

    private HubStatsDto convertToStatsDto(Hub hub) {
        HubStatsDto dto = new HubStatsDto();
        dto.setHubId(hub.getHubId());
        dto.setHubName(hub.getName());
        dto.setCity(hub.getCity());
        dto.setTotalAgents(agentRepository.countByHubId(hub.getHubId()));
        dto.setActiveAgents(agentRepository.countByHubIdAndAvailabilityStatus(
                hub.getHubId(), Agent.AvailabilityStatus.AVAILABLE));
        dto.setTotalDeliveries(deliveryRepository.countByHubId(hub.getHubId()));
        dto.setTodayDeliveries(deliveryRepository.countTodayDeliveriesByHub(hub.getHubId()));
        return dto;
    }

    private HubPerformanceDto calculateHubPerformance(Hub hub) {
        Long hubId = hub.getHubId();

        HubPerformanceDto dto = new HubPerformanceDto();
        dto.setHubId(hubId);
        dto.setHubName(hub.getName());

        // Calculate delivery statistics
        Long totalDeliveries = deliveryRepository.countByHubId(hubId);
        Long successfulDeliveries = deliveryRepository.countByHubIdAndStatus(
                hubId, Delivery.DeliveryStatus.DELIVERED);
        Long failedDeliveries = deliveryRepository.countByHubIdAndStatus(
                hubId, Delivery.DeliveryStatus.FAILED);

        dto.setTotalDeliveries(totalDeliveries);
        dto.setSuccessfulDeliveries(successfulDeliveries);
        dto.setFailedDeliveries(failedDeliveries);

        if (totalDeliveries > 0) {
            dto.setSuccessRate((double) (successfulDeliveries * 100) / totalDeliveries);
        } else {
            dto.setSuccessRate(100.0);
        }

        // Calculate efficiency based on average delivery time
        Double avgDeliveryTime = deliveryRepository.getAverageDeliveryTimeByHub(hubId);
        dto.setAvgDeliveryTime(avgDeliveryTime != null ? avgDeliveryTime : 0.0);

        // Calculate efficiency score (example: 100 - (avgTime - 30) if avgTime > 30)
        if (avgDeliveryTime != null && avgDeliveryTime > 0) {
            double efficiency = Math.max(60.0, 100.0 - Math.max(0, avgDeliveryTime - 30));
            dto.setEfficiencyScore(efficiency);
        } else {
            dto.setEfficiencyScore(100.0);
        }

        return dto;
    }

    private AgentResponseDto convertAgentToResponseDto(Agent agent) {
        // This would be similar to the one in AgentService
        AgentResponseDto dto = new AgentResponseDto();
        // ... conversion logic
        return dto;
    }

    private DeliveryResponseDto convertDeliveryToResponseDto(Delivery delivery) {
        // This would be similar to the one in DeliveryService
        DeliveryResponseDto dto = new DeliveryResponseDto();
        // ... conversion logic
        return dto;
    }
}