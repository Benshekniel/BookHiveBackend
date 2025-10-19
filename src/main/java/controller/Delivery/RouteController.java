// RouteController.java - Fixed Complete Version
package controller.Delivery;

import model.dto.Delivery.DeliveryDto;
import service.Delivery.impl.DeliveryService;
import service.Delivery.impl.RouteAssignmentService;
import model.dto.Delivery.RouteDTO;
import model.dto.Delivery.RouteDTO.*;
import service.Delivery.impl.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Main RouteController class containing all route-related REST endpoints
 */
@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private static final Logger logger = LoggerFactory.getLogger(RouteController.class);

    @Autowired
    private RouteService routeService;

    @Autowired(required = false)
    private DeliveryService deliveryService;

    @Autowired(required = false)
    private RouteAssignmentService routeAssignmentService;

    // ================================
    // MAIN ROUTE ENDPOINTS
    // ================================

    @GetMapping
    public ResponseEntity<List<RouteDTO>> getAllRoutes() {
        try {
            logger.debug("Fetching all routes");
            List<RouteDTO> routes = routeService.getAllRoutes();
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            logger.error("Error fetching all routes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<RouteDTO> getRouteById(@PathVariable Long routeId) {
        try {
            logger.debug("Fetching route with ID: {}", routeId);
            Optional<RouteDTO> route = routeService.getRouteById(routeId);
            return route.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error fetching route with ID: {}", routeId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/hub/{hubId}")
    public ResponseEntity<List<RouteDTO>> getRoutesByHub(@PathVariable Long hubId) {
        try {
            logger.debug("Fetching routes for hub ID: {}", hubId);
            List<RouteDTO> routes = routeService.getRoutesByHub(hubId);
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            logger.error("Error fetching routes for hub ID: {}", hubId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RouteDTO>> getRoutesByStatus(@PathVariable String status) {
        try {
            logger.debug("Fetching routes with status: {}", status);
            List<RouteDTO> routes = routeService.getRoutesByStatus(status);
            return ResponseEntity.ok(routes);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid route status: {}", status, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error fetching routes by status: {}", status, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/type/{routeType}")
    public ResponseEntity<List<RouteDTO>> getRoutesByType(@PathVariable String routeType) {
        try {
            logger.debug("Fetching routes with type: {}", routeType);
            List<RouteDTO> routes = routeService.getRoutesByType(routeType);
            return ResponseEntity.ok(routes);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid route type: {}", routeType, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error fetching routes by type: {}", routeType, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createRoute(@Valid @RequestBody RouteCreateDTO createDTO) {
        try {
            logger.info("Creating new route: {}", createDTO.getName());

            // Validation
            if (createDTO.getHubId() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Hub ID is required");
                return ResponseEntity.badRequest().body(error);
            }

            if (createDTO.getName() == null || createDTO.getName().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Route name is required");
                return ResponseEntity.badRequest().body(error);
            }

            RouteDTO createdRoute = routeService.createRoute(createDTO);
            logger.info("Successfully created route with ID: {}", createdRoute.getRouteId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoute);

        } catch (IllegalArgumentException e) {
            logger.error("Validation error creating route: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            logger.error("Error creating route: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create route: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{routeId}")
    public ResponseEntity<?> updateRoute(
            @PathVariable Long routeId,
            @Valid @RequestBody RouteUpdateDTO updateDTO) {
        try {
            logger.info("Updating route with ID: {}", routeId);
            Optional<RouteDTO> updatedRoute = routeService.updateRoute(routeId, updateDTO);

            if (updatedRoute.isPresent()) {
                logger.info("Successfully updated route with ID: {}", routeId);
                return ResponseEntity.ok(updatedRoute.get());
            } else {
                logger.warn("Route not found for update: {}", routeId);
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Validation error updating route {}: {}", routeId, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            logger.error("Error updating route {}: {}", routeId, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update route: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{routeId}/status")
    public ResponseEntity<?> updateRouteStatus(
            @PathVariable Long routeId,
            @RequestBody StatusUpdateRequest request) {
        try {
            logger.info("Updating route status for ID: {} to {}", routeId, request.getStatus());
            Optional<RouteDTO> updatedRoute = routeService.updateRouteStatus(routeId, request.getStatus());

            if (updatedRoute.isPresent()) {
                logger.info("Successfully updated route status for ID: {}", routeId);
                return ResponseEntity.ok(updatedRoute.get());
            } else {
                logger.warn("Route not found for status update: {}", routeId);
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid status for route {}: {}", routeId, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            logger.error("Error updating route status {}: {}", routeId, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update route status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{routeId}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long routeId) {
        try {
            logger.info("Deleting route with ID: {}", routeId);
            boolean deleted = routeService.deleteRoute(routeId);

            if (deleted) {
                logger.info("Successfully deleted route with ID: {}", routeId);
                return ResponseEntity.noContent().build();
            } else {
                logger.warn("Route not found for deletion: {}", routeId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deleting route {}: {}", routeId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ================================
    // ROUTE ASSIGNMENT ENDPOINTS
    // ================================

    @GetMapping("/{routeId}/agents")
    public ResponseEntity<List<RouteAssignmentDTO>> getRouteAgents(@PathVariable Long routeId) {
        try {
            logger.debug("Fetching agents for route ID: {}", routeId);
            List<RouteAssignmentDTO> assignments = routeService.getAssignmentsByRoute(routeId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            logger.error("Error fetching agents for route {}: {}", routeId, e.getMessage(), e);
            // Return empty list instead of error to prevent frontend issues
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/{routeId}/assign-agent")
    public ResponseEntity<?> assignAgentToRoute(
            @PathVariable Long routeId,
            @Valid @RequestBody AgentAssignmentRequestDTO requestDTO) {
        try {
            logger.info("Assigning agent {} to route {}", requestDTO.getAgentId(), routeId);
            RouteAssignmentDTO assignment = routeService.assignAgentToRoute(routeId, requestDTO);
            logger.info("Successfully assigned agent {} to route {}", requestDTO.getAgentId(), routeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error assigning agent {} to route {}: {}",
                    requestDTO.getAgentId(), routeId, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            logger.error("Error assigning agent {} to route {}: {}",
                    requestDTO.getAgentId(), routeId, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to assign agent: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{routeId}/remove-agent/{agentId}")
    public ResponseEntity<Void> removeAgentFromRoute(
            @PathVariable Long routeId,
            @PathVariable Long agentId) {
        try {
            logger.info("Removing agent {} from route {}", agentId, routeId);
            boolean removed = routeService.removeAgentFromRoute(routeId, agentId);

            if (removed) {
                logger.info("Successfully removed agent {} from route {}", agentId, routeId);
                return ResponseEntity.noContent().build();
            } else {
                logger.warn("Agent assignment not found: agent {} route {}", agentId, routeId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error removing agent {} from route {}: {}", agentId, routeId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{routeId}/assign-multiple-agents")
    public ResponseEntity<?> assignMultipleAgents(
            @PathVariable Long routeId,
            @RequestBody MultipleAgentAssignmentRequest request) {
        try {
            logger.info("Assigning {} agents to route {}", request.getAgentIds().size(), routeId);
            List<RouteAssignmentDTO> assignments = routeService.assignMultipleAgents(routeId, request.getAgentIds());
            return ResponseEntity.status(HttpStatus.CREATED).body(assignments);
        } catch (Exception e) {
            logger.error("Error assigning multiple agents to route {}: {}", routeId, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to assign agents: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ================================
    // BOUNDARY COORDINATE ENDPOINTS
    // ================================

    @GetMapping("/{routeId}/boundaries")
    public ResponseEntity<List<RouteBoundaryDTO>> getRouteBoundaries(@PathVariable Long routeId) {
        try {
            logger.debug("Fetching boundaries for route ID: {}", routeId);
            List<RouteBoundaryDTO> boundaries = routeService.getRouteBoundaries(routeId);
            return ResponseEntity.ok(boundaries);
        } catch (Exception e) {
            logger.error("Error fetching boundaries for route {}: {}", routeId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{routeId}/boundaries")
    public ResponseEntity<?> updateRouteBoundaries(
            @PathVariable Long routeId,
            @RequestBody RouteBoundaryUpdateRequest request) {
        try {
            logger.info("Updating boundaries for route ID: {}", routeId);
            Optional<RouteBoundaryDTO> updatedBoundary = routeService.updateRouteBoundaries(
                    routeId, request.getBoundaryCoordinates());

            if (updatedBoundary.isPresent()) {
                logger.info("Successfully updated boundaries for route ID: {}", routeId);
                return ResponseEntity.ok(updatedBoundary.get());
            } else {
                logger.warn("Route not found for boundary update: {}", routeId);
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Validation error updating boundaries for route {}: {}", routeId, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            logger.error("Error updating boundaries for route {}: {}", routeId, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update boundaries: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ================================
    // ADDITIONAL ENDPOINTS
    // ================================

    @GetMapping("/search")
    public ResponseEntity<List<RouteDTO>> searchRoutes(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String postalCode,
            @RequestParam(required = false) String neighborhood,
            @RequestParam(required = false) String routeType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long hubId,
            @RequestParam(required = false) String trafficPattern,
            @RequestParam(required = false) Integer priorityLevel) {
        try {
            logger.debug("Searching routes with criteria");
            RouteSearchDTO searchDTO = new RouteSearchDTO();
            searchDTO.setName(name);
            searchDTO.setPostalCode(postalCode);
            searchDTO.setNeighborhood(neighborhood);
            searchDTO.setRouteType(routeType);
            searchDTO.setStatus(status);
            searchDTO.setHubId(hubId);
            searchDTO.setTrafficPattern(trafficPattern);
            searchDTO.setPriorityLevel(priorityLevel);

            List<RouteDTO> routes = routeService.searchRoutes(searchDTO);
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            logger.error("Error searching routes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/postal-code/{postalCode}")
    public ResponseEntity<List<RouteDTO>> getRouteByPostalCode(@PathVariable String postalCode) {
        try {
            logger.debug("Fetching routes for postal code: {}", postalCode);
            List<RouteDTO> routes = routeService.getRouteByPostalCode(postalCode);
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            logger.error("Error fetching routes by postal code {}: {}", postalCode, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<RouteDTO>> getNearbyRoutes(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radius) {
        try {
            logger.debug("Fetching routes near coordinates: {}, {} within {} km", latitude, longitude, radius);
            List<RouteDTO> routes = routeService.getNearbyRoutes(latitude, longitude, radius);
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            logger.error("Error fetching nearby routes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/postal-codes/available")
    public ResponseEntity<List<String>> getAvailablePostalCodes(@RequestParam Long hubId) {
        try {
            logger.debug("Fetching available postal codes for hub: {}", hubId);
            List<String> availableCodes = routeService.getAvailablePostalCodes(hubId);
            return ResponseEntity.ok(availableCodes);
        } catch (Exception e) {
            logger.error("Error fetching available postal codes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/validate-coverage")
    public ResponseEntity<PostalCodeValidationDTO> validateRouteCoverage(@RequestBody RouteCreateDTO routeData) {
        try {
            logger.debug("Validating route coverage");
            PostalCodeValidationDTO validation = routeService.validateRouteCoverage(routeData);
            return ResponseEntity.ok(validation);
        } catch (Exception e) {
            logger.error("Error validating route coverage: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{routeId}/optimization-suggestions")
    public ResponseEntity<List<RouteOptimizationSuggestionDTO>> getOptimizationSuggestions(@PathVariable Long routeId) {
        try {
            logger.debug("Fetching optimization suggestions for route: {}", routeId);
            List<RouteOptimizationSuggestionDTO> suggestions = routeService.getOptimizationSuggestions(routeId);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            logger.error("Error fetching optimization suggestions: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/analytics/hub/{hubId}")
    public ResponseEntity<RouteAnalyticsDTO> getRouteAnalytics(
            @PathVariable Long hubId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
        try {
            logger.debug("Fetching route analytics for hub: {}", hubId);
            RouteAnalyticsDTO analytics = routeService.getRouteAnalytics(hubId, dateFrom, dateTo);
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            logger.error("Error fetching route analytics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ================================
    // EXCEPTION HANDLING
    // ================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.warn("Validation errors: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        logger.warn("Illegal argument: {}", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "An unexpected error occurred");
        error.put("details", ex.getMessage());
        logger.error("Unexpected error in RouteController", ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}