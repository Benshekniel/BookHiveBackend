package service.Delivery.impl;

import model.entity.Agent;
import model.entity.AllUsers;
import model.entity.Hub;
import model.dto.Delivery.AgentDto.*;
import model.repo.Delivery.AgentRepository;
import model.repo.Delivery.HubRepository;
import model.repo.Delivery.DeliveryRepository;
import model.repo.AllUsersRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AgentService {

    private final AgentRepository agentRepository;
    private final HubRepository hubRepository;
    private final DeliveryRepository deliveryRepository;
    private final AllUsersRepo allUsersRepo;

    public AgentResponseDto createAgent(AgentCreateDto createDto) {
        log.info("Creating agent for user: {} in hub: {}", createDto.getUserId(), createDto.getHubId());

        // Validate hub exists
        Hub hub = hubRepository.findById(createDto.getHubId())
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        // Validate user exists
        AllUsers user = allUsersRepo.findById(createDto.getUserId().intValue())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is already an agent
        if (agentRepository.existsByUserId(createDto.getUserId())) {
            throw new RuntimeException("User is already an agent");
        }

        // Create agent
        Agent agent = new Agent();
        agent.setUserId(createDto.getUserId());
        agent.setHubId(createDto.getHubId());
        if (createDto.getPhoneNumber() != null) {
            agent.setPhoneNumber(createDto.getPhoneNumber());
        }
        if (createDto.getVehicleType() != null) {
            agent.setVehicleType(createDto.getVehicleType());
        }
        if (createDto.getVehicleNumber() != null) {
            agent.setVehicleNumber(createDto.getVehicleNumber());
        }
        agent.setAvailabilityStatus(Agent.AvailabilityStatus.AVAILABLE);
        agent.setTrustScore(80.0); // Default trust score

        // Update user role
        user.setRole("AGENT");
        allUsersRepo.save(user);

        Agent savedAgent = agentRepository.save(agent);
        log.info("Created agent with ID: {}", savedAgent.getAgentId());

        return convertToResponseDto(savedAgent);
    }

    public List<AgentResponseDto> getAllAgents() {
        log.info("Fetching all agents with details");
        List<Object[]> results = agentRepository.findAllAgentsWithDetails();
        return results.stream()
                .map(this::convertToResponseDtoWithDetails)
                .collect(Collectors.toList());
    }

    public List<AgentResponseDto> getAvailableAgents() {
        return agentRepository.findByAvailabilityStatus(Agent.AvailabilityStatus.AVAILABLE).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<AgentResponseDto> getAgentById(Long agentId) {
        return agentRepository.findAgentWithDetails(agentId)
                .map(this::convertToResponseDtoWithDetails);
    }

    public Optional<AgentResponseDto> getAgentByUserId(Long userId) {
        return agentRepository.findByUserId(userId)
                .map(this::convertToResponseDto);
    }

    public List<AgentResponseDto> getAgentsByHub(Long hubId) {
        return agentRepository.findAgentsByHubWithDetails(hubId).stream()
                .map(this::convertToResponseDtoWithDetails)
                .collect(Collectors.toList());
    }

    public List<AgentResponseDto> getAvailableAgentsByHub(Long hubId) {
        return agentRepository.findByHubIdAndAvailabilityStatus(hubId, Agent.AvailabilityStatus.AVAILABLE).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public AgentResponseDto updateAgentStatus(Long agentId, Agent.AvailabilityStatus status) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        agent.setAvailabilityStatus(status);
        Agent updatedAgent = agentRepository.save(agent);

        log.info("Updated agent {} status to {}", agentId, status);
        return convertToResponseDto(updatedAgent);
    }

    public AgentResponseDto updateAgent(Long agentId, AgentUpdateDto updateDto) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        // Update agent fields
        if (updateDto.getPhone() != null) {
            agent.setPhoneNumber(updateDto.getPhone());
        }
        if (updateDto.getVehicleType() != null) {
            try {
                agent.setVehicleType(Agent.VehicleType.valueOf(updateDto.getVehicleType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid vehicle type: {}", updateDto.getVehicleType());
            }
        }
        if (updateDto.getVehicleNumber() != null) {
            agent.setVehicleNumber(updateDto.getVehicleNumber());
        }
        if (updateDto.getAvailabilityStatus() != null) {
            try {
                agent.setAvailabilityStatus(Agent.AvailabilityStatus.valueOf(updateDto.getAvailabilityStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid availability status: {}", updateDto.getAvailabilityStatus());
            }
        }

        // Update user details if needed
        if (updateDto.getName() != null) {
            allUsersRepo.findById(agent.getUserId().intValue())
                    .ifPresent(user -> {
                        user.setName(updateDto.getName());
                        allUsersRepo.save(user);
                    });
        }

        Agent updatedAgent = agentRepository.save(agent);
        log.info("Updated agent {}", agentId);
        return convertToResponseDto(updatedAgent);
    }

    public AgentPerformanceDto getAgentPerformance(Long agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        return calculateAgentPerformance(agent);
    }

    public List<AgentPerformanceDto> getAgentPerformanceByHub(Long hubId) {
        List<Agent> agents = agentRepository.findByHubId(hubId);
        return agents.stream()
                .map(this::calculateAgentPerformance)
                .collect(Collectors.toList());
    }

    public void deleteAgent(Long agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        // Check if agent has active deliveries
        long activeDeliveries = deliveryRepository.countByAgentIdAndStatusIn(agentId,
                List.of(model.entity.Delivery.DeliveryStatus.ASSIGNED,
                        model.entity.Delivery.DeliveryStatus.PICKED_UP,
                        model.entity.Delivery.DeliveryStatus.IN_TRANSIT));

        if (activeDeliveries > 0) {
            throw new RuntimeException("Cannot delete agent with active deliveries");
        }

        // Update user role back to USER
        allUsersRepo.findById(agent.getUserId().intValue())
                .ifPresent(user -> {
                    user.setRole("USER");
                    allUsersRepo.save(user);
                });

        agentRepository.deleteById(agentId);
        log.info("Deleted agent {}", agentId);
    }

    // Helper methods
    private AgentResponseDto convertToResponseDto(Agent agent) {
        AgentResponseDto dto = new AgentResponseDto();

        // Basic agent info
        dto.setAgentId(agent.getAgentId());
        dto.setId(agent.getAgentId()); // For frontend compatibility
        dto.setUserId(agent.getUserId());
        dto.setHubId(agent.getHubId());
        dto.setPhoneNumber(agent.getPhoneNumber());
        dto.setUserPhone(agent.getPhoneNumber());
        dto.setVehicleType(agent.getVehicleType());
        dto.setVehicleNumber(agent.getVehicleNumber());
        dto.setAvailabilityStatus(agent.getAvailabilityStatus());
        dto.setTrustScore(agent.getTrustScore());
        dto.setRating(agent.getTrustScore() != null ? agent.getTrustScore() / 20.0 : 4.0); // Convert to 5-star rating

        // Get user details
        if (agent.getUserId() != null) {
            allUsersRepo.findById(agent.getUserId().intValue())
                    .ifPresent(user -> {
                        dto.setUserName(user.getName());
                        dto.setName(user.getName()); // For frontend compatibility
                        dto.setUserEmail(user.getEmail());
                        dto.setEmail(user.getEmail()); // For frontend compatibility
                    });
        }

        // Get hub details
        if (agent.getHubId() != null) {
            hubRepository.findById(agent.getHubId())
                    .ifPresent(hub -> {
                        dto.setHubName(hub.getName());
                        dto.setHubCity(hub.getCity());
                    });
        }

        // Calculate delivery stats
        Long totalDeliveries = deliveryRepository.countByAgentId(agent.getAgentId());
        dto.setTotalDeliveries(totalDeliveries);
        dto.setNumberOfDelivery(totalDeliveries.intValue());

        return dto;
    }

    private AgentResponseDto convertToResponseDtoWithDetails(Object[] result) {
        AgentResponseDto dto = new AgentResponseDto();

        Agent agent = (Agent) result[0];
        String userName = result[1] != null ? result[1].toString() : "Unknown User";
        String userEmail = result[2] != null ? result[2].toString() : "N/A";
        String phoneNumber = result[3] != null ? result[3].toString() : agent.getPhoneNumber();
        String hubName = result[4] != null ? result[4].toString() : "Unknown Hub";
        Long deliveryCount = result[5] != null ? ((Number) result[5]).longValue() : 0L;

        // Basic agent info
        dto.setAgentId(agent.getAgentId());
        dto.setId(agent.getAgentId()); // For frontend compatibility
        dto.setUserId(agent.getUserId());
        dto.setHubId(agent.getHubId());
        dto.setPhoneNumber(phoneNumber);
        dto.setUserPhone(phoneNumber);
        dto.setVehicleType(agent.getVehicleType());
        dto.setVehicleNumber(agent.getVehicleNumber());
        dto.setAvailabilityStatus(agent.getAvailabilityStatus());
        dto.setTrustScore(agent.getTrustScore());
        dto.setRating(agent.getTrustScore() != null ? agent.getTrustScore() / 20.0 : 4.0);

        // User details
        dto.setUserName(userName);
        dto.setName(userName); // For frontend compatibility
        dto.setUserEmail(userEmail);
        dto.setEmail(userEmail); // For frontend compatibility

        // Hub details
        dto.setHubName(hubName);

        // Get hub city
        if (agent.getHubId() != null) {
            hubRepository.findById(agent.getHubId())
                    .ifPresent(hub -> dto.setHubCity(hub.getCity()));
        }

        // Delivery stats
        dto.setTotalDeliveries(deliveryCount);
        dto.setNumberOfDelivery(deliveryCount.intValue());

        return dto;
    }

    private AgentPerformanceDto calculateAgentPerformance(Agent agent) {
        AgentPerformanceDto dto = new AgentPerformanceDto();
        dto.setAgentId(agent.getAgentId());
        dto.setAgentName("Agent " + agent.getAgentId());

        // Get user name
        if (agent.getUserId() != null) {
            allUsersRepo.findById(agent.getUserId().intValue())
                    .ifPresent(user -> {
                        dto.setAgentName(user.getName());
                        dto.setName(user.getName()); // For frontend compatibility
                    });
        }

        // Calculate performance metrics
        Long totalDeliveries = deliveryRepository.countByAgentId(agent.getAgentId());
        Long successfulDeliveries = deliveryRepository.countByAgentIdAndStatus(
                agent.getAgentId(), model.entity.Delivery.DeliveryStatus.DELIVERED);
        Long failedDeliveries = deliveryRepository.countByAgentIdAndStatus(
                agent.getAgentId(), model.entity.Delivery.DeliveryStatus.FAILED);

        dto.setTotalDeliveries(totalDeliveries);
        dto.setSuccessfulDeliveries(successfulDeliveries);
        dto.setFailedDeliveries(failedDeliveries);
        dto.setDeliveries(totalDeliveries.intValue()); // For frontend compatibility

        // Calculate success rate
        if (totalDeliveries > 0) {
            dto.setSuccessRate((double) (successfulDeliveries * 100) / totalDeliveries);
        } else {
            dto.setSuccessRate(100.0);
        }

        // Calculate average delivery time (mock for now)
        dto.setAvgDeliveryTime(24.5); // hours
        dto.setAvgTime(25); // For frontend compatibility

        dto.setTrustScore(agent.getTrustScore());
        dto.setRating(agent.getTrustScore() != null ? agent.getTrustScore() / 20.0 : 4.0);
        dto.setAvailabilityStatus(agent.getAvailabilityStatus());

        return dto;
    }
}