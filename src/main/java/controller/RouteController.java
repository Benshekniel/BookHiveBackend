// RouteController.java - All Route-related Controllers consolidated into one file
package controller;

import model.dto.Hubmanager.RouteDTO;
import model.dto.Hubmanager.RouteDTO.*;
import service.Hubmanager.impl.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

/**
 * Main RouteController class containing all route-related REST endpoints
 */
@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    // ================================
    // MAIN ROUTE ENDPOINTS
    // ================================

    @GetMapping
    public ResponseEntity<List<RouteDTO>> getAllRoutes() {
        List<RouteDTO> routes = routeService.getAllRoutes();
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<RouteDTO> getRouteById(@PathVariable Long routeId) {
        Optional<RouteDTO> route = routeService.getRouteById(routeId);
        return route.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/hub/{hubId}")
    public ResponseEntity<List<RouteDTO>> getRoutesByHub(@PathVariable Long hubId) {
        List<RouteDTO> routes = routeService.getRoutesByHub(hubId);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RouteDTO>> getRoutesByStatus(@PathVariable String status) {
        try {
            List<RouteDTO> routes = routeService.getRoutesByStatus(status);
            return ResponseEntity.ok(routes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/type/{routeType}")
    public ResponseEntity<List<RouteDTO>> getRoutesByType(@PathVariable String routeType) {
        try {
            List<RouteDTO> routes = routeService.getRoutesByType(routeType);
            return ResponseEntity.ok(routes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/postal-code/{postalCode}")
    public ResponseEntity<List<RouteDTO>> getRouteByPostalCode(@PathVariable String postalCode) {
        List<RouteDTO> routes = routeService.getRouteByPostalCode(postalCode);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<RouteDTO>> getNearbyRoutes(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radius) {
        List<RouteDTO> routes = routeService.getNearbyRoutes(latitude, longitude, radius);
        return ResponseEntity.ok(routes);
    }

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
    }

    @PostMapping
    public ResponseEntity<RouteDTO> createRoute(@Valid @RequestBody RouteCreateDTO createDTO) {
        try {
            RouteDTO createdRoute = routeService.createRoute(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoute);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{routeId}")
    public ResponseEntity<RouteDTO> updateRoute(
            @PathVariable Long routeId,
            @Valid @RequestBody RouteUpdateDTO updateDTO) {
        Optional<RouteDTO> updatedRoute = routeService.updateRoute(routeId, updateDTO);
        return updatedRoute.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{routeId}/status")
    public ResponseEntity<RouteDTO> updateRouteStatus(
            @PathVariable Long routeId,
            @RequestBody StatusUpdateRequest request) {
        Optional<RouteDTO> updatedRoute = routeService.updateRouteStatus(routeId, request.getStatus());
        return updatedRoute.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{routeId}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long routeId) {
        boolean deleted = routeService.deleteRoute(routeId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<List<RouteDTO>> bulkCreateRoutes(@Valid @RequestBody BulkRouteCreateDTO bulkCreateDTO) {
        try {
            List<RouteDTO> createdRoutes = routeService.bulkCreateRoutes(bulkCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoutes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/bulk-update-status")
    public ResponseEntity<List<RouteDTO>> bulkUpdateStatus(@RequestBody BulkStatusUpdateRequest request) {
        try {
            List<RouteDTO> updatedRoutes = routeService.bulkUpdateStatus(request.getRouteIds(), request.getStatus());
            return ResponseEntity.ok(updatedRoutes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/postal-codes/available")
    public ResponseEntity<List<String>> getAvailablePostalCodes(@RequestParam Long hubId) {
        List<String> availableCodes = routeService.getAvailablePostalCodes(hubId);
        return ResponseEntity.ok(availableCodes);
    }

    @PostMapping("/validate-coverage")
    public ResponseEntity<PostalCodeValidationDTO> validateRouteCoverage(@RequestBody RouteCreateDTO routeData) {
        PostalCodeValidationDTO validation = routeService.validateRouteCoverage(routeData);
        return ResponseEntity.ok(validation);
    }

    @GetMapping("/{routeId}/optimization-suggestions")
    public ResponseEntity<List<RouteOptimizationSuggestionDTO>> getOptimizationSuggestions(@PathVariable Long routeId) {
        List<RouteOptimizationSuggestionDTO> suggestions = routeService.getOptimizationSuggestions(routeId);
        return ResponseEntity.ok(suggestions);
    }

    // ================================
    // ROUTE ASSIGNMENT ENDPOINTS
    // ================================

    @GetMapping("/{routeId}/agents")
    public ResponseEntity<List<RouteAssignmentDTO>> getRouteAgents(@PathVariable Long routeId) {
        List<RouteAssignmentDTO> assignments = routeService.getAssignmentsByRoute(routeId);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/{routeId}/assign-agent")
    public ResponseEntity<RouteAssignmentDTO> assignAgentToRoute(
            @PathVariable Long routeId,
            @Valid @RequestBody AgentAssignmentRequestDTO requestDTO) {
        try {
            RouteAssignmentDTO assignment = routeService.assignAgentToRoute(routeId, requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{routeId}/remove-agent/{agentId}")
    public ResponseEntity<Void> removeAgentFromRoute(
            @PathVariable Long routeId,
            @PathVariable Long agentId) {
        boolean removed = routeService.removeAgentFromRoute(routeId, agentId);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{routeId}/assign-multiple-agents")
    public ResponseEntity<List<RouteAssignmentDTO>> assignMultipleAgents(
            @PathVariable Long routeId,
            @RequestBody MultipleAgentAssignmentRequest request) {
        try {
            List<RouteAssignmentDTO> assignments =
                    routeService.assignMultipleAgents(routeId, request.getAgentIds());
            return ResponseEntity.status(HttpStatus.CREATED).body(assignments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ================================
    // ROUTE PERFORMANCE ENDPOINTS
    // ================================

    @GetMapping("/{routeId}/performance")
    public ResponseEntity<List<RoutePerformanceDTO>> getRoutePerformance(@PathVariable Long routeId) {
        List<RoutePerformanceDTO> performance = routeService.getPerformanceByRoute(routeId);
        return ResponseEntity.ok(performance);
    }

    @GetMapping("/analytics/hub/{hubId}")
    public ResponseEntity<RouteAnalyticsDTO> getRouteAnalytics(
            @PathVariable Long hubId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {

        RouteAnalyticsDTO analytics = routeService.getRouteAnalytics(hubId, dateFrom, dateTo);
        return ResponseEntity.ok(analytics);
    }

    // ================================
    // ROUTE BOUNDARY ENDPOINTS
    // ================================

    @GetMapping("/{routeId}/boundaries")
    public ResponseEntity<List<RouteBoundaryDTO>> getRouteBoundaries(@PathVariable Long routeId) {
        List<RouteBoundaryDTO> boundaries = routeService.getRouteBoundaries(routeId);
        return ResponseEntity.ok(boundaries);
    }

    @PutMapping("/{routeId}/boundaries")
    public ResponseEntity<RouteBoundaryDTO> updateRouteBoundaries(
            @PathVariable Long routeId,
            @RequestBody RouteBoundaryUpdateRequest request) {
        Optional<RouteBoundaryDTO> updatedBoundary = routeService.updateRouteBoundaries(
                routeId, request.getBoundaryCoordinates());
        return updatedBoundary.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // ================================
    // ROUTE ASSIGNMENT MANAGEMENT ENDPOINTS
    // ================================

    @GetMapping("/assignments/route/{routeId}")
    public ResponseEntity<List<RouteAssignmentDTO>> getAssignmentsByRoute(@PathVariable Long routeId) {
        List<RouteAssignmentDTO> assignments = routeService.getAssignmentsByRoute(routeId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/assignments/agent/{agentId}")
    public ResponseEntity<List<RouteAssignmentDTO>> getAssignmentsByAgent(@PathVariable Long agentId) {
        List<RouteAssignmentDTO> assignments = routeService.getAssignmentsByAgent(agentId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/assignments/hub/{hubId}")
    public ResponseEntity<List<RouteAssignmentDTO>> getAssignmentsByHub(@PathVariable Long hubId) {
        List<RouteAssignmentDTO> assignments = routeService.getAssignmentsByHub(hubId);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/assignments")
    public ResponseEntity<RouteAssignmentDTO> createAssignment(
            @RequestParam Long routeId,
            @Valid @RequestBody AgentAssignmentRequestDTO requestDTO) {
        try {
            RouteAssignmentDTO assignment = routeService.assignAgentToRoute(routeId, requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/assignments/{assignmentId}/status")
    public ResponseEntity<RouteAssignmentDTO> updateAssignmentStatus(
            @PathVariable Long assignmentId,
            @RequestBody StatusUpdateRequest request) {
        Optional<RouteAssignmentDTO> updatedAssignment =
                routeService.updateAssignmentStatus(assignmentId, request.getStatus());
        return updatedAssignment.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/assignments/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long assignmentId) {
        boolean deleted = routeService.deleteAssignment(assignmentId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ================================
    // EXPORT/IMPORT ENDPOINTS (Future Implementation)
    // ================================

    @GetMapping("/export/csv")
    public ResponseEntity<String> exportRoutesToCSV(
            @RequestParam Long hubId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String routeType) {
        // Future implementation for CSV export
        return ResponseEntity.ok("CSV Export functionality - To be implemented");
    }

    @PostMapping("/import/csv")
    public ResponseEntity<Map<String, Object>> importRoutesFromCSV(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam Long hubId) {
        // Future implementation for CSV import
        Map<String, Object> response = new HashMap<>();
        response.put("message", "CSV Import functionality - To be implemented");
        response.put("filename", file.getOriginalFilename());
        response.put("hubId", hubId);
        return ResponseEntity.ok(response);
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
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "An unexpected error occurred");
        error.put("details", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}