// RouteService.java - Fixed for Simple Route Entity (No Nested Classes)
package service.Hubmanager.impl;

import model.dto.Hubmanager.RouteDTO;
import model.dto.Hubmanager.RouteDTO.*;
import model.entity.Route;
import model.entity.Agent;
import model.repo.Hubmanager.RouteRepository;
import model.repo.Hubmanager.AgentRepository;
import model.repo.AllUsersRepo;
import model.entity.AllUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
@Transactional
public class RouteService {

    private static final Logger logger = LoggerFactory.getLogger(RouteService.class);

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AllUsersRepo allUsersRepository;

    // ================================
    // BASIC CRUD OPERATIONS
    // ================================

    @Transactional(readOnly = true)
    public List<RouteDTO> getAllRoutes() {
        logger.debug("Fetching all routes");
        return routeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RouteDTO> getRouteById(Long routeId) {
        logger.debug("Fetching route with ID: {}", routeId);
        return routeRepository.findById(routeId)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public List<RouteDTO> getRoutesByHub(Long hubId) {
        logger.debug("Fetching routes for hub ID: {}", hubId);
        return routeRepository.findByHubId(hubId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RouteDTO> getRoutesByStatus(String status) {
        logger.debug("Fetching routes with status: {}", status);
        try {
            Route.RouteStatus routeStatus = Route.RouteStatus.valueOf(status.toUpperCase());
            return routeRepository.findByStatus(routeStatus).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid route status: {}", status);
            throw new IllegalArgumentException("Invalid route status: " + status);
        }
    }

    @Transactional(readOnly = true)
    public List<RouteDTO> getRoutesByType(String routeType) {
        logger.debug("Fetching routes with type: {}", routeType);
        try {
            Route.RouteType type = Route.RouteType.valueOf(routeType.toUpperCase());
            return routeRepository.findByRouteType(type).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid route type: {}", routeType);
            throw new IllegalArgumentException("Invalid route type: " + routeType);
        }
    }

    @Transactional(readOnly = true)
    public List<RouteDTO> getRouteByPostalCode(String postalCode) {
        logger.debug("Fetching routes for postal code: {}", postalCode);
        return routeRepository.findByPostalCode(postalCode).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RouteDTO> getNearbyRoutes(Double latitude, Double longitude, Double radius) {
        logger.debug("Fetching routes near coordinates: {}, {} within {} km", latitude, longitude, radius);
        return routeRepository.findNearbyRoutes(latitude, longitude, radius).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RouteDTO> searchRoutes(RouteSearchDTO searchDTO) {
        logger.debug("Searching routes with criteria: {}", searchDTO);

        Route.RouteType routeType = null;
        Route.RouteStatus status = null;
        Route.TrafficPattern trafficPattern = null;

        try {
            if (searchDTO.getRouteType() != null) {
                routeType = Route.RouteType.valueOf(searchDTO.getRouteType().toUpperCase());
            }
            if (searchDTO.getStatus() != null) {
                status = Route.RouteStatus.valueOf(searchDTO.getStatus().toUpperCase());
            }
            if (searchDTO.getTrafficPattern() != null) {
                trafficPattern = Route.TrafficPattern.valueOf(searchDTO.getTrafficPattern().toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid enum value in search criteria: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid search criteria: " + e.getMessage());
        }

        return routeRepository.searchRoutes(
                searchDTO.getName(),
                searchDTO.getPostalCode(),
                searchDTO.getNeighborhood(),
                routeType,
                status,
                searchDTO.getHubId(),
                trafficPattern,
                searchDTO.getPriorityLevel()
        ).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public RouteDTO createRoute(RouteCreateDTO createDTO) {
        logger.info("Creating new route: {}", createDTO.getName());

        if (createDTO.getHubId() == null) {
            throw new IllegalArgumentException("Hub ID is required");
        }

        Route route = new Route();
        mapCreateDTOToEntity(createDTO, route);
        route = routeRepository.save(route);

        logger.info("Created route with ID: {}", route.getRouteId());
        return convertToDTO(route);
    }

    public Optional<RouteDTO> updateRoute(Long routeId, RouteUpdateDTO updateDTO) {
        logger.info("Updating route with ID: {}", routeId);

        return routeRepository.findById(routeId).map(route -> {
            mapUpdateDTOToEntity(updateDTO, route);
            route = routeRepository.save(route);
            logger.info("Updated route with ID: {}", routeId);
            return convertToDTO(route);
        });
    }

    public Optional<RouteDTO> updateRouteStatus(Long routeId, String status) {
        logger.info("Updating route status for ID: {} to {}", routeId, status);

        return routeRepository.findById(routeId).map(route -> {
            try {
                route.setStatus(Route.RouteStatus.valueOf(status.toUpperCase()));
                route = routeRepository.save(route);
                logger.info("Updated route status for ID: {} to {}", routeId, status);
                return convertToDTO(route);
            } catch (IllegalArgumentException e) {
                logger.error("Invalid route status: {}", status);
                throw new IllegalArgumentException("Invalid route status: " + status);
            }
        });
    }

    public boolean deleteRoute(Long routeId) {
        logger.info("Deleting route with ID: {}", routeId);

        if (routeRepository.existsById(routeId)) {
            routeRepository.deleteById(routeId);
            logger.info("Deleted route with ID: {}", routeId);
            return true;
        }

        logger.warn("Route with ID: {} not found for deletion", routeId);
        return false;
    }

    // ================================
    // BULK OPERATIONS
    // ================================

    public List<RouteDTO> bulkCreateRoutes(BulkRouteCreateDTO bulkCreateDTO) {
        logger.info("Bulk creating {} routes", bulkCreateDTO.getRoutes().size());

        List<Route> routes = bulkCreateDTO.getRoutes().stream()
                .map(createDTO -> {
                    Route route = new Route();
                    mapCreateDTOToEntity(createDTO, route);
                    return route;
                })
                .collect(Collectors.toList());

        routes = routeRepository.saveAll(routes);
        logger.info("Bulk created {} routes", routes.size());

        return routes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<RouteDTO> bulkUpdateStatus(List<Long> routeIds, String status) {
        logger.info("Bulk updating status for {} routes to {}", routeIds.size(), status);

        try {
            Route.RouteStatus routeStatus = Route.RouteStatus.valueOf(status.toUpperCase());
            List<Route> routes = routeRepository.findAllById(routeIds);

            routes.forEach(route -> route.setStatus(routeStatus));
            routes = routeRepository.saveAll(routes);

            logger.info("Bulk updated status for {} routes", routes.size());
            return routes.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid route status for bulk update: {}", status);
            throw new IllegalArgumentException("Invalid route status: " + status);
        }
    }

    // ================================
    // ASSIGNMENT OPERATIONS (Simplified - No Nested Classes)
    // ================================

    @Transactional(readOnly = true)
    public List<RouteAssignmentDTO> getAssignmentsByRoute(Long routeId) {
        logger.debug("Fetching assignments for route ID: {}", routeId);

        // Validate route exists
        Optional<Route> routeOpt = routeRepository.findById(routeId);
        if (!routeOpt.isPresent()) {
            logger.error("Route not found with ID: {}", routeId);
            throw new IllegalArgumentException("Route not found with ID: " + routeId);
        }

        Route route = routeOpt.get();

        // Get agents assigned to this route
        List<Agent> agents = routeRepository.findByRouteId(routeId);

        // Convert to RouteAssignmentDTO list
        return agents.stream().map(agent -> {
            RouteAssignmentDTO assignment = new RouteAssignmentDTO();
            assignment.setRouteId(routeId);
            assignment.setRouteName(route.getName());
            assignment.setAgentId(agent.getAgentId());

            // Get user details from AllUsers using userId
            Optional<AllUsers> userOpt = allUsersRepository.findById(agent.getUserId().intValue());
            if (userOpt.isPresent()) {
                AllUsers user = userOpt.get();
                assignment.setAgentName(user.getName());
                assignment.setAssignedByName(user.getName()); // Assuming assignedByName uses same user details
            } else {
                assignment.setAgentName("Unknown Agent");
                assignment.setAssignedByName("Unknown");
                logger.warn("User not found for agent ID: {} with userId: {}",
                        agent.getAgentId(), agent.getUserId());
            }

            // Set assignment details
            assignment.setStatus("ACTIVE");
            assignment.setAssignedAt(LocalDateTime.now());
            assignment.setAgentVehicleType(agent.getVehicleType() != null ?
                    agent.getVehicleType().toString() : null);
            assignment.setAgentVehicleNumber(agent.getVehicleNumber());
            assignment.setAgentAvailabilityStatus(agent.getAvailabilityStatus() != null ?
                    agent.getAvailabilityStatus().toString() : null);

            return assignment;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RouteAssignmentDTO> getAssignmentsByAgent(Long agentId) {
        logger.debug("Fetching assignments for agent ID: {}", agentId);
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<RouteAssignmentDTO> getAssignmentsByHub(Long hubId) {
        logger.debug("Fetching assignments for hub ID: {}", hubId);
        return new ArrayList<>();
    }

    public RouteAssignmentDTO assignAgentToRoute(Long routeId, AgentAssignmentRequestDTO requestDTO) {
        logger.info("Assigning agent {} to route {}", requestDTO.getAgentId(), routeId);

        // Validate route exists
        if (!routeRepository.existsById(routeId)) {
            logger.error("Route not found with ID: {}", routeId);
            throw new IllegalArgumentException("Route not found with ID: " + routeId);
        }

        // Validate agent exists
        if (!agentRepository.existsById(requestDTO.getAgentId())) {
            logger.error("Agent not found with ID: {}", requestDTO.getAgentId());
            throw new IllegalArgumentException("Agent not found with ID: " + requestDTO.getAgentId());
        }

        // Create a simple assignment DTO (without persisting to database for now)
        RouteAssignmentDTO assignment = new RouteAssignmentDTO();
        assignment.setRouteId(routeId);
        assignment.setAgentId(requestDTO.getAgentId());
        assignment.setStatus("ACTIVE");
        assignment.setStartDate(requestDTO.getStartDate() != null ? requestDTO.getStartDate() : LocalDate.now());
        assignment.setAssignedAt(LocalDateTime.now());

        logger.info("Successfully assigned agent {} to route {}", requestDTO.getAgentId(), routeId);
        return assignment;
    }

    public boolean removeAgentFromRoute(Long routeId, Long agentId) {
        logger.info("Removing agent {} from route {}", agentId, routeId);
        // Simplified implementation - return true for now
        return true;
    }

    public List<RouteAssignmentDTO> assignMultipleAgents(Long routeId, List<Long> agentIds) {
        logger.info("Assigning {} agents to route {}", agentIds.size(), routeId);

        List<RouteAssignmentDTO> assignments = new ArrayList<>();
        for (Long agentId : agentIds) {
            AgentAssignmentRequestDTO requestDTO = new AgentAssignmentRequestDTO();
            requestDTO.setAgentId(agentId);
            try {
                RouteAssignmentDTO assignment = assignAgentToRoute(routeId, requestDTO);
                assignments.add(assignment);
            } catch (Exception e) {
                logger.warn("Failed to assign agent {} to route {}: {}", agentId, routeId, e.getMessage());
            }
        }

        return assignments;
    }

    public Optional<RouteAssignmentDTO> updateAssignmentStatus(Long assignmentId, String status) {
        logger.info("Updating assignment {} status to {}", assignmentId, status);
        // Simplified implementation
        return Optional.empty();
    }

    public boolean deleteAssignment(Long assignmentId) {
        logger.info("Deleting assignment with ID: {}", assignmentId);
        // Simplified implementation
        return true;
    }

    // ================================
    // PERFORMANCE OPERATIONS (Simplified)
    // ================================

    @Transactional(readOnly = true)
    public List<RoutePerformanceDTO> getPerformanceByRoute(Long routeId) {
        logger.debug("Fetching performance data for route ID: {}", routeId);
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public RouteAnalyticsDTO getRouteAnalytics(Long hubId, LocalDate dateFrom, LocalDate dateTo) {
        logger.debug("Generating analytics for hub {} from {} to {}", hubId, dateFrom, dateTo);

        if (dateFrom == null) dateFrom = LocalDate.now().minusDays(30);
        if (dateTo == null) dateTo = LocalDate.now();

        RouteAnalyticsDTO analytics = new RouteAnalyticsDTO();
        analytics.setHubId(hubId);
        analytics.setDateFrom(dateFrom);
        analytics.setDateTo(dateTo);

        // Get basic route counts
        Long totalRoutes = routeRepository.countByHubId(hubId);
        Long activeRoutes = routeRepository.countByHubIdAndStatus(hubId, Route.RouteStatus.ACTIVE);

        analytics.setTotalRoutes(totalRoutes.intValue());
        analytics.setActiveRoutes(activeRoutes.intValue());
        analytics.setTotalDeliveries(0);
        analytics.setSuccessfulDeliveries(0);
        analytics.setOverallEfficiency(BigDecimal.ZERO);
        analytics.setAverageDeliveryTime(BigDecimal.ZERO);

        return analytics;
    }

    // ================================
    // BOUNDARY OPERATIONS (Simplified)
    // ================================

    @Transactional(readOnly = true)
    public List<RouteBoundaryDTO> getRouteBoundaries(Long routeId) {
        logger.debug("Fetching boundaries for route ID: {}", routeId);
        return new ArrayList<>();
    }

    public Optional<RouteBoundaryDTO> updateRouteBoundaries(Long routeId, String boundaryCoordinates) {
        logger.info("Updating boundaries for route ID: {}", routeId);

        Optional<Route> routeOpt = routeRepository.findById(routeId);
        if (routeOpt.isPresent()) {
            Route route = routeOpt.get();
            route.setBoundaryCoordinates(boundaryCoordinates);
            routeRepository.save(route);

            RouteBoundaryDTO boundary = new RouteBoundaryDTO();
            boundary.setRouteId(routeId);
            boundary.setCoordinates(boundaryCoordinates);
            boundary.setBoundaryType("PRIMARY");
            boundary.setIsActive(true);
            boundary.setUpdatedAt(LocalDateTime.now());
            boundary.setRouteName(route.getName());

            return Optional.of(boundary);
        }

        return Optional.empty();
    }

    // ================================
    // UTILITY OPERATIONS
    // ================================

    @Transactional(readOnly = true)
    public List<String> getAvailablePostalCodes(Long hubId) {
        logger.debug("Fetching available postal codes for hub ID: {}", hubId);
        List<String> postalCodeStrings = routeRepository.getUsedPostalCodesByHub(hubId);

        // Parse comma-separated postal codes into individual codes
        List<String> individualCodes = new ArrayList<>();
        for (String postalCodeString : postalCodeStrings) {
            if (postalCodeString != null && !postalCodeString.trim().isEmpty()) {
                String[] codes = postalCodeString.split(",\\s*");
                for (String code : codes) {
                    String trimmedCode = code.trim();
                    if (!trimmedCode.isEmpty() && !individualCodes.contains(trimmedCode)) {
                        individualCodes.add(trimmedCode);
                    }
                }
            }
        }

        return individualCodes;
    }

    @Transactional(readOnly = true)
    public PostalCodeValidationDTO validateRouteCoverage(RouteCreateDTO routeData) {
        logger.debug("Validating route coverage for postal codes: {}", routeData.getPostalCodes());

        PostalCodeValidationDTO validation = new PostalCodeValidationDTO();
        validation.setIsValid(true);
        validation.setIsAvailable(true);

        if (routeData.getPostalCodes() != null && !routeData.getPostalCodes().trim().isEmpty()) {
            List<Route> existingRoutes = routeRepository.findByPostalCode(routeData.getPostalCodes());
            if (!existingRoutes.isEmpty()) {
                Route existingRoute = existingRoutes.get(0);
                validation.setIsAvailable(false);
                validation.setExistingRouteId(existingRoute.getRouteId().toString());
                validation.setExistingRouteName(existingRoute.getName());
                logger.warn("Postal code {} already assigned to route {}",
                        routeData.getPostalCodes(), existingRoute.getName());
            }
        }

        return validation;
    }

    @Transactional(readOnly = true)
    public List<RouteOptimizationSuggestionDTO> getOptimizationSuggestions(Long routeId) {
        logger.debug("Generating optimization suggestions for route ID: {}", routeId);

        List<RouteOptimizationSuggestionDTO> suggestions = new ArrayList<>();

        Optional<Route> routeOpt = routeRepository.findById(routeId);
        if (routeOpt.isPresent()) {
            Route route = routeOpt.get();

            // Basic optimization suggestions based on route properties
            if (route.getMaxDailyDeliveries() == null || route.getMaxDailyDeliveries() < 20) {
                RouteOptimizationSuggestionDTO suggestion = new RouteOptimizationSuggestionDTO();
                suggestion.setRouteId(routeId);
                suggestion.setRouteName(route.getName());
                suggestion.setSuggestionType("COVERAGE");
                suggestion.setTitle("Expand Route Coverage");
                suggestion.setDescription("Route has low delivery capacity. Consider expanding coverage area or merging with nearby routes.");
                suggestion.setPriority("LOW");
                suggestion.setPotentialImprovement(15.0);
                suggestion.setActionItems(List.of(
                        "Analyze nearby uncovered areas",
                        "Review route consolidation options",
                        "Assess demand patterns"
                ));
                suggestions.add(suggestion);
            }

            // Add agent allocation suggestion
            RouteOptimizationSuggestionDTO agentSuggestion = new RouteOptimizationSuggestionDTO();
            agentSuggestion.setRouteId(routeId);
            agentSuggestion.setRouteName(route.getName());
            agentSuggestion.setSuggestionType("AGENT_ALLOCATION");
            agentSuggestion.setTitle("Review Agent Allocation");
            agentSuggestion.setDescription("Ensure optimal agent assignment for this route.");
            agentSuggestion.setPriority("MEDIUM");
            agentSuggestion.setPotentialImprovement(20.0);
            agentSuggestion.setActionItems(List.of(
                    "Review available agents",
                    "Match agent skills with route requirements",
                    "Optimize agent schedules"
            ));
            suggestions.add(agentSuggestion);
        }

        logger.debug("Generated {} optimization suggestions for route {}", suggestions.size(), routeId);
        return suggestions;
    }

    // ================================
    // CONVERSION METHODS
    // ================================

    private RouteDTO convertToDTO(Route route) {
        RouteDTO dto = new RouteDTO();
        dto.setRouteId(route.getRouteId());
        dto.setName(route.getName());
        dto.setDescription(route.getDescription());
        dto.setHubId(route.getHubId());
        dto.setCoverageArea(route.getCoverageArea());
        dto.setPostalCodes(route.getPostalCodes());
        dto.setStatus(route.getStatus() != null ? route.getStatus().toString() : null);
        dto.setCenterLatitude(route.getCenterLatitude());
        dto.setCenterLongitude(route.getCenterLongitude());
        dto.setBoundaryCoordinates(route.getBoundaryCoordinates());
        dto.setEstimatedDeliveryTime(route.getEstimatedDeliveryTime());
        dto.setMaxDailyDeliveries(route.getMaxDailyDeliveries());
        dto.setPriorityLevel(route.getPriorityLevel());
        dto.setNeighborhoods(route.getNeighborhoods());
        dto.setLandmarks(route.getLandmarks());
        dto.setTrafficPattern(route.getTrafficPattern() != null ? route.getTrafficPattern().toString() : null);
        dto.setRouteType(route.getRouteType() != null ? route.getRouteType().toString() : null);
        dto.setVehicleRestrictions(route.getVehicleRestrictions());
        dto.setCreatedAt(route.getCreatedAt());
        dto.setUpdatedAt(route.getUpdatedAt());
        dto.setCreatedBy(route.getCreatedBy());

        // Set default values for calculated fields
        dto.setAssignedRiders(0);
        dto.setTotalRiders(0);
        dto.setDailyDeliveries(0);
        dto.setAverageDeliveryTime(0.0);
        dto.setEfficiency(0.0);
        dto.setAgents(new ArrayList<>());

        return dto;
    }

    private AgentDTO convertAgentToDTO(Agent agent) {
        AgentDTO dto = new AgentDTO();
        dto.setAgentId(agent.getAgentId());
        dto.setHubId(agent.getHubId());
        dto.setUserId(agent.getUserId());
        dto.setVehicleType(agent.getVehicleType() != null ? agent.getVehicleType().toString() : null);
        dto.setVehicleNumber(agent.getVehicleNumber());
        dto.setAvailabilityStatus(agent.getAvailabilityStatus() != null ? agent.getAvailabilityStatus().toString() : null);
        dto.setTrustScore(agent.getTrustScore());
        dto.setDeliveryTime(agent.getDeliveryTime());
        dto.setNumberOfDelivery(agent.getNumberOfDelivery());

        // Get user details from AllUsers entity using userId
        try {
            Optional<AllUsers> userOpt = allUsersRepository.findById(agent.getUserId().intValue());
            if (userOpt.isPresent()) {
                AllUsers user = userOpt.get();
                dto.setName(user.getName());
                dto.setEmail(user.getEmail());
                dto.setPhoneNumber(user.getPhoneNumber());

                // Split name into first and last name if possible
                if (user.getName() != null && !user.getName().trim().isEmpty()) {
                    String[] nameParts = user.getName().trim().split(" ", 2);
                    dto.setFirstName(nameParts[0]);
                    dto.setLastName(nameParts.length > 1 ? nameParts[1] : "");
                } else {
                    dto.setFirstName("");
                    dto.setLastName("");
                }
            } else {
                // Set default values if user not found
                dto.setName("Unknown User");
                dto.setEmail("");
                dto.setPhoneNumber("");
                dto.setFirstName("Unknown");
                dto.setLastName("User");
                logger.warn("User not found for agent {} with userId {}", agent.getAgentId(), agent.getUserId());
            }
        } catch (Exception e) {
            logger.error("Error fetching user details for agent {}: {}", agent.getAgentId(), e.getMessage());
            // Set default values in case of error
            dto.setName("Error Loading User");
            dto.setEmail("");
            dto.setPhoneNumber("");
            dto.setFirstName("Error");
            dto.setLastName("Loading");
        }

        return dto;
    }

    private void mapCreateDTOToEntity(RouteCreateDTO createDTO, Route route) {
        route.setName(createDTO.getName());
        route.setDescription(createDTO.getDescription());
        route.setHubId(createDTO.getHubId());
        route.setCoverageArea(createDTO.getCoverageArea());
        route.setPostalCodes(createDTO.getPostalCodes());
        route.setCenterLatitude(createDTO.getCenterLatitude());
        route.setCenterLongitude(createDTO.getCenterLongitude());
        route.setBoundaryCoordinates(createDTO.getBoundaryCoordinates());
        route.setEstimatedDeliveryTime(createDTO.getEstimatedDeliveryTime());
        route.setMaxDailyDeliveries(createDTO.getMaxDailyDeliveries());
        route.setPriorityLevel(createDTO.getPriorityLevel() != null ? createDTO.getPriorityLevel() : 3);
        route.setNeighborhoods(createDTO.getNeighborhoods());
        route.setLandmarks(createDTO.getLandmarks());
        route.setVehicleRestrictions(createDTO.getVehicleRestrictions());
        route.setCreatedBy(createDTO.getCreatedBy());

        // Set enums with validation
        if (createDTO.getTrafficPattern() != null && !createDTO.getTrafficPattern().trim().isEmpty()) {
            try {
                route.setTrafficPattern(Route.TrafficPattern.valueOf(createDTO.getTrafficPattern().toUpperCase()));
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid traffic pattern: {}", createDTO.getTrafficPattern());
                route.setTrafficPattern(Route.TrafficPattern.MODERATE);
            }
        }

        if (createDTO.getRouteType() != null && !createDTO.getRouteType().trim().isEmpty()) {
            try {
                route.setRouteType(Route.RouteType.valueOf(createDTO.getRouteType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid route type: {}", createDTO.getRouteType());
                route.setRouteType(Route.RouteType.MIXED);
            }
        }
    }

    private void mapUpdateDTOToEntity(RouteUpdateDTO updateDTO, Route route) {
        if (updateDTO.getName() != null && !updateDTO.getName().trim().isEmpty()) {
            route.setName(updateDTO.getName());
        }
        if (updateDTO.getDescription() != null) {
            route.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getCoverageArea() != null) {
            route.setCoverageArea(updateDTO.getCoverageArea());
        }
        if (updateDTO.getPostalCodes() != null) {
            route.setPostalCodes(updateDTO.getPostalCodes());
        }
        if (updateDTO.getCenterLatitude() != null) {
            route.setCenterLatitude(updateDTO.getCenterLatitude());
        }
        if (updateDTO.getCenterLongitude() != null) {
            route.setCenterLongitude(updateDTO.getCenterLongitude());
        }
        if (updateDTO.getBoundaryCoordinates() != null) {
            route.setBoundaryCoordinates(updateDTO.getBoundaryCoordinates());
        }
        if (updateDTO.getEstimatedDeliveryTime() != null) {
            route.setEstimatedDeliveryTime(updateDTO.getEstimatedDeliveryTime());
        }
        if (updateDTO.getMaxDailyDeliveries() != null) {
            route.setMaxDailyDeliveries(updateDTO.getMaxDailyDeliveries());
        }
        if (updateDTO.getPriorityLevel() != null) {
            route.setPriorityLevel(updateDTO.getPriorityLevel());
        }
        if (updateDTO.getNeighborhoods() != null) {
            route.setNeighborhoods(updateDTO.getNeighborhoods());
        }
        if (updateDTO.getLandmarks() != null) {
            route.setLandmarks(updateDTO.getLandmarks());
        }
        if (updateDTO.getVehicleRestrictions() != null) {
            route.setVehicleRestrictions(updateDTO.getVehicleRestrictions());
        }

        // Update enums with validation
        if (updateDTO.getTrafficPattern() != null && !updateDTO.getTrafficPattern().trim().isEmpty()) {
            try {
                route.setTrafficPattern(Route.TrafficPattern.valueOf(updateDTO.getTrafficPattern().toUpperCase()));
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid traffic pattern in update: {}", updateDTO.getTrafficPattern());
            }
        }

        if (updateDTO.getRouteType() != null && !updateDTO.getRouteType().trim().isEmpty()) {
            try {
                route.setRouteType(Route.RouteType.valueOf(updateDTO.getRouteType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid route type in update: {}", updateDTO.getRouteType());
            }
        }
    }
}