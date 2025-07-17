package service.Hubmanager.impl;

import model.entity.AllUsers;
import model.entity.Agent;
import model.entity.Hub;
import model.entity.Delivery;
import model.dto.Hubmanager.AgentDto.*;
import model.repo.Hubmanager.AgentRepository;
import model.repo.AllUsersRepo;
import model.repo.Hubmanager.HubRepository;
import model.repo.Hubmanager.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AgentService {

    private final AgentRepository agentRepository;
    private final AllUsersRepo allUsersRepo;
    private final HubRepository hubRepository;
    private final DeliveryRepository deliveryRepository;

    // Optimized getAllAgents using JOIN query
    public List<AgentResponseDto> getAllAgents() {
        List<Object[]> results = agentRepository.findAllAgentsWithDetails();

        return results.stream()
                .map(this::convertToResponseDtoFromJoinQuery)
                .collect(Collectors.toList());
    }

    // Fallback method using individual queries (keep for backward compatibility)
    public List<AgentResponseDto> getAllAgentsOriginal() {
        return agentRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // Optimized method for single agent
    public Optional<AgentResponseDto> getAgentByIdOptimized(Long agentId) {
        return agentRepository.findAgentWithDetails(agentId)
                .map(this::convertToResponseDtoFromJoinQuery);
    }

    // Optimized method for agents by hub
    public List<AgentResponseDto> getAgentsByHubOptimized(Long hubId) {
        List<Object[]> results = agentRepository.findAgentsByHubWithDetails(hubId);

        return results.stream()
                .map(this::convertToResponseDtoFromJoinQuery)
                .collect(Collectors.toList());
    }

    // Convert JOIN query result to DTO
    private AgentResponseDto convertToResponseDtoFromJoinQuery(Object[] result) {
        Agent agent = (Agent) result[0];
        String userName = (String) result[1];
        String userEmail = (String) result[2];
        String userPhone = (String) result[3];
        String hubName = (String) result[4];
        Long totalDeliveries = (Long) result[5];

        AgentResponseDto dto = new AgentResponseDto();

        // Basic agent info
        dto.setAgentId(agent.getAgentId());
        dto.setId(agent.getAgentId()); // For frontend compatibility
        dto.setUserId(agent.getUserId());

        // User details from JOIN query
        dto.setName(userName != null ? userName : "Unknown Agent");
        dto.setUserName(userName != null ? userName : "Unknown Agent");
        dto.setEmail(userEmail != null ? userEmail : "N/A");
        dto.setUserEmail(userEmail != null ? userEmail : "N/A");
        dto.setPhoneNumber(userPhone != null ? userPhone : "N/A");

        // Hub details from JOIN query
        dto.setHubId(agent.getHubId());
        dto.setHubName(hubName != null ? hubName : "Unknown Hub");

        // Agent specific details
        dto.setVehicleType(agent.getVehicleType());
        dto.setVehicleNumber(agent.getVehicleNumber());
        dto.setAvailabilityStatus(agent.getAvailabilityStatus());
        dto.setTrustScore(agent.getTrustScore());
        dto.setDeliveryTime(agent.getDeliveryTime());
        dto.setNumberOfDelivery(agent.getNumberOfDelivery());

        // Total deliveries from JOIN query
        dto.setTotalDeliveries(totalDeliveries != null ? totalDeliveries.intValue() : 0);

        // Calculate rating (convert 0-100 trust score to 0-5 rating)
        dto.setRating(agent.getTrustScore() != null ? agent.getTrustScore() / 20.0 : 0.0);

        return dto;
    }

    // Original method (keep for backward compatibility)
    private AgentResponseDto convertToResponseDto(Agent agent) {
        AgentResponseDto dto = new AgentResponseDto();
        dto.setAgentId(agent.getAgentId());
        dto.setId(agent.getAgentId()); // For frontend compatibility

        // Fetch User entity
        AllUsers user = allUsersRepo.findById((int) agent.getUserId().longValue())
                .orElseThrow(() -> new RuntimeException("User not found"));
        dto.setUserId((long) user.getUser_id());
        dto.setName(user.getName());
        dto.setUserName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setUserEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());

        // Fetch Hub entity
        Hub hub = hubRepository.findById(agent.getHubId())
                .orElseThrow(() -> new RuntimeException("Hub not found"));
        dto.setHubId(hub.getHubId());
        dto.setHubName(hub.getName());

        dto.setVehicleType(agent.getVehicleType());
        dto.setVehicleNumber(agent.getVehicleNumber());
        dto.setAvailabilityStatus(agent.getAvailabilityStatus());
        dto.setTrustScore(agent.getTrustScore());
        dto.setDeliveryTime(agent.getDeliveryTime());
        dto.setNumberOfDelivery(agent.getNumberOfDelivery());

        // Get total deliveries from database
        Long totalDeliveries = deliveryRepository.countByAgentId(agent.getAgentId());
        dto.setTotalDeliveries(totalDeliveries != null ? totalDeliveries.intValue() : 0);

        // Calculate rating (convert 0-100 trust score to 0-5 rating)
        dto.setRating(agent.getTrustScore() != null ? agent.getTrustScore() / 20.0 : 0.0);

        return dto;
    }

    // Keep all other existing methods unchanged
    public AgentResponseDto createAgent(AgentCreateDto createDto) {
        AllUsers user = allUsersRepo.findById((int) createDto.getUserId().longValue())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Hub hub = hubRepository.findById(createDto.getHubId())
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        // Check if user is already an agent
        if (agentRepository.existsByUserId(createDto.getUserId())) {
            throw new RuntimeException("User is already registered as an agent");
        }

        Agent agent = new Agent();
        agent.setUserId((long) user.getUser_id());
        agent.setHubId(hub.getHubId());
        agent.setVehicleType(createDto.getVehicleType());
        agent.setVehicleNumber(createDto.getVehicleNumber());
        agent.setAvailabilityStatus(Agent.AvailabilityStatus.AVAILABLE);
        agent.setTrustScore(0.0);
        agent.setDeliveryTime(0);
        agent.setNumberOfDelivery(0);

        // Update user role
        user.setRole("DELIVERY_AGENT");
        allUsersRepo.save(user);

        Agent savedAgent = agentRepository.save(agent);
        return convertToResponseDto(savedAgent);
    }

    public List<AgentResponseDto> getAgentsByHub(Long hubId) {
        return agentRepository.findByHubId(hubId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<AgentResponseDto> getAvailableAgents() {
        return agentRepository.findByAvailabilityStatus(Agent.AvailabilityStatus.AVAILABLE).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<AgentResponseDto> getAvailableAgentsByHub(Long hubId) {
        return agentRepository.findByHubIdAndAvailabilityStatus(hubId, Agent.AvailabilityStatus.AVAILABLE).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<AgentResponseDto> getAgentById(Long agentId) {
        return agentRepository.findById(agentId)
                .map(this::convertToResponseDto);
    }

    public Optional<AgentResponseDto> getAgentByUserId(Long userId) {
        return agentRepository.findByUserId(userId)
                .map(this::convertToResponseDto);
    }

    public AgentResponseDto updateAgentStatus(Long agentId, Agent.AvailabilityStatus status) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        agent.setAvailabilityStatus(status);
        Agent updatedAgent = agentRepository.save(agent);
        return convertToResponseDto(updatedAgent);
    }

    public AgentResponseDto updateAgentInfo(Long agentId, AgentUpdateDto updateDto) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        if (updateDto.getVehicleType() != null) {
            agent.setVehicleType(Agent.VehicleType.valueOf(updateDto.getVehicleType()));
        }
        if (updateDto.getVehicleNumber() != null) {
            agent.setVehicleNumber(updateDto.getVehicleNumber());
        }
        if (updateDto.getAvailabilityStatus() != null) {
            agent.setAvailabilityStatus(Agent.AvailabilityStatus.valueOf(updateDto.getAvailabilityStatus()));
        }

        Agent updatedAgent = agentRepository.save(agent);
        return convertToResponseDto(updatedAgent);
    }

    public void updateDeliveryStats(Long agentId, Integer deliveryTime) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        int currentDeliveries = agent.getNumberOfDelivery();
        int currentTotalTime = agent.getDeliveryTime() * currentDeliveries;
        int newTotalTime = currentTotalTime + deliveryTime;
        int newDeliveryCount = currentDeliveries + 1;

        agent.setNumberOfDelivery(newDeliveryCount);
        agent.setDeliveryTime(newTotalTime / newDeliveryCount); // Average delivery time

        agentRepository.save(agent);
    }

    public void updateTrustScore(Long agentId, Double newScore) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        agent.setTrustScore(newScore);
        agentRepository.save(agent);
    }

    public List<AgentPerformanceDto> getAgentPerformanceByHub(Long hubId) {
        List<Agent> agents = agentRepository.findByHubId(hubId);
        return agents.stream()
                .map(this::convertToPerformanceDto)
                .collect(Collectors.toList());
    }

    public void deleteAgent(Long agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        // Check if agent has active deliveries
        long activeDeliveries = deliveryRepository.countByAgentIdAndStatusIn(
                agentId,
                List.of(Delivery.DeliveryStatus.ASSIGNED, Delivery.DeliveryStatus.PICKED_UP, Delivery.DeliveryStatus.IN_TRANSIT)
        );

        if (activeDeliveries > 0) {
            throw new RuntimeException("Cannot delete agent with active deliveries");
        }

        // Reset user role if needed
        AllUsers user = allUsersRepo.findById((int) agent.getUserId().longValue())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole("USER");
        allUsersRepo.save(user);

        agentRepository.deleteById(agentId);
    }

    private AgentPerformanceDto convertToPerformanceDto(Agent agent) {
        AgentPerformanceDto dto = new AgentPerformanceDto();
        dto.setAgentId(agent.getAgentId());

        // Fetch User entity
        AllUsers user = allUsersRepo.findById((int) agent.getUserId().longValue())
                .orElseThrow(() -> new RuntimeException("User not found"));
        dto.setName(user.getName());

        dto.setDeliveries(agent.getNumberOfDelivery());
        dto.setAvgTime(agent.getDeliveryTime());
        dto.setRating(agent.getTrustScore() / 20.0);

        // Calculate success rate (this could be more sophisticated)
        Long totalDeliveries = deliveryRepository.countByAgentId(agent.getAgentId());
        Long successfulDeliveries = deliveryRepository.countByAgentIdAndStatus(
                agent.getAgentId(), Delivery.DeliveryStatus.DELIVERED
        );

        if (totalDeliveries > 0) {
            dto.setSuccessRate((double) (successfulDeliveries * 100) / totalDeliveries);
        } else {
            dto.setSuccessRate(100.0);
        }

        return dto;
    }
}