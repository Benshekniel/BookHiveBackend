package service.Delivery.impl;

import model.entity.AllUsers;
import model.entity.Agent;
import model.entity.Hub;
import model.entity.Delivery;
import model.dto.Delivery.AgentDto.*;
import model.repo.Delivery.AgentRepository;
import model.repo.AllUsersRepo;
import model.repo.Delivery.HubRepository;
import model.repo.Delivery.DeliveryRepository;
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

    // Convert JOIN query result to DTO (updated indices to match new query)
    private AgentResponseDto convertToResponseDtoFromJoinQuery(Object[] result) {
        Agent agent = (Agent) result[0];
        String userName = result[1] != null ? result[1].toString() : "Unknown Agent";
        String userEmail = result[2] != null ? result[2].toString() : "N/A";
        String agentPhone = result[3] != null ? result[3].toString() : "N/A"; // From Agent table
        String hubName = result[4] != null ? result[4].toString() : "Unknown Hub";
        Long totalDeliveries = result[5] != null ? ((Number) result[5]).longValue() : 0L;

        AgentResponseDto dto = new AgentResponseDto();

        // Basic agent info
        dto.setAgentId(agent.getAgentId());
        dto.setId(agent.getAgentId()); // For frontend compatibility
        dto.setUserId(agent.getUserId());

        // User details from JOIN query
        dto.setName(userName);
        dto.setUserName(userName);
        dto.setEmail(userEmail);
        dto.setUserEmail(userEmail);
        dto.setUserPhone(agentPhone); // Phone from Agent table as String

        // Convert String phone to Integer for DTO compatibility
        dto.setPhoneNumber(convertPhoneToInteger(agent.getPhoneNumber()));

        // Hub details from JOIN query
        dto.setHubId(agent.getHubId());
        dto.setHubName(hubName);

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

        // Use phone number from Agent entity
        dto.setUserPhone(agent.getPhoneNumber() != null ? agent.getPhoneNumber() : "N/A");
        dto.setPhoneNumber(convertPhoneToInteger(agent.getPhoneNumber()));

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

    // Helper method to convert String phone to Integer
    private Integer convertPhoneToInteger(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return null;
        }
        try {
            // Remove all non-digit characters and convert to Integer
            String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");
            if (digitsOnly.isEmpty()) {
                return null;
            }
            return Integer.valueOf(digitsOnly);
        } catch (NumberFormatException e) {
            return null; // Return null if conversion fails
        }
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
        // Add phone number update if needed
        if (updateDto.getPhone() != null) {
            agent.setPhoneNumber(updateDto.getPhone()); // Store as String in entity
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
        dto.setAgentName(user.getName());

        dto.setDeliveries(agent.getNumberOfDelivery());
        dto.setAvgTime(agent.getDeliveryTime());
        dto.setRating(agent.getTrustScore() / 20.0);
        dto.setTrustScore(agent.getTrustScore());

        // Calculate delivery statistics
        Long totalDeliveries = deliveryRepository.countByAgentId(agent.getAgentId());
        Long successfulDeliveries = deliveryRepository.countByAgentIdAndStatus(
                agent.getAgentId(), Delivery.DeliveryStatus.DELIVERED
        );
        Long failedDeliveries = deliveryRepository.countByAgentIdAndStatus(
                agent.getAgentId(), Delivery.DeliveryStatus.FAILED
        );

        dto.setTotalDeliveries(totalDeliveries);
        dto.setSuccessfulDeliveries(successfulDeliveries);
        dto.setFailedDeliveries(failedDeliveries);

        if (totalDeliveries > 0) {
            dto.setSuccessRate((double) (successfulDeliveries * 100) / totalDeliveries);
        } else {
            dto.setSuccessRate(100.0);
        }

        dto.setAvgDeliveryTime(agent.getDeliveryTime() != null ? agent.getDeliveryTime().doubleValue() : 0.0);

        return dto;
    }
}