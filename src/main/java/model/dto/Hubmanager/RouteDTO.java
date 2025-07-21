// RouteDTO.java - All Route-related DTOs consolidated into one file
package model.dto.Hubmanager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * Main RouteDTO for route data transfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {
    private Long routeId;
    private String name;
    private String description;
    private Long hubId;
    private String coverageArea;
    private String postalCodes;
    private String status;
    private Double centerLatitude;
    private Double centerLongitude;
    private String boundaryCoordinates;
    private Integer estimatedDeliveryTime;
    private Integer maxDailyDeliveries;
    private Integer priorityLevel;
    private String neighborhoods;
    private String landmarks;
    private String trafficPattern;
    private String routeType;
    private String vehicleRestrictions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;

    // Calculated fields
    private Integer dailyDeliveries;
    private Integer assignedRiders;
    private Integer totalRiders;
    private Double averageDeliveryTime;
    private List<AgentDTO> agents;
    private Double efficiency;
    private String hubName;
    private String createdByName;

    /**
     * RouteCreateDTO for creating new routes
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteCreateDTO {
        @NotBlank(message = "Route name is required")
        private String name;

        private String description;

        @NotNull(message = "Hub ID is required")
        private Long hubId;

        private String coverageArea;
        private String postalCodes;
        private Double centerLatitude;
        private Double centerLongitude;
        private String boundaryCoordinates;

        @Min(value = 1, message = "Estimated delivery time must be greater than 0")
        private Integer estimatedDeliveryTime;

        @Min(value = 1, message = "Max daily deliveries must be greater than 0")
        private Integer maxDailyDeliveries;

        @Min(value = 1, message = "Priority level must be between 1 and 5")
        @Max(value = 5, message = "Priority level must be between 1 and 5")
        private Integer priorityLevel;

        private String neighborhoods;
        private String landmarks;
        private String trafficPattern;
        private String routeType;
        private String vehicleRestrictions;
        private Long createdBy;
    }

    /**
     * RouteUpdateDTO for updating existing routes
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteUpdateDTO {
        private String name;
        private String description;
        private String coverageArea;
        private String postalCodes;
        private Double centerLatitude;
        private Double centerLongitude;
        private String boundaryCoordinates;

        @Min(value = 1, message = "Estimated delivery time must be greater than 0")
        private Integer estimatedDeliveryTime;

        @Min(value = 1, message = "Max daily deliveries must be greater than 0")
        private Integer maxDailyDeliveries;

        @Min(value = 1, message = "Priority level must be between 1 and 5")
        @Max(value = 5, message = "Priority level must be between 1 and 5")
        private Integer priorityLevel;

        private String neighborhoods;
        private String landmarks;
        private String trafficPattern;
        private String routeType;
        private String vehicleRestrictions;
    }

    /**
     * RouteAssignmentDTO for route-agent assignments
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteAssignmentDTO {
        private Long assignmentId;
        private Long routeId;
        private Long agentId;
        private LocalDateTime assignedAt;
        private Long assignedBy;
        private String status;
        private LocalDate startDate;

        // Additional fields for display
        private String routeName;
        private String agentName;
        private String assignedByName;
        private String agentVehicleType;
        private String agentVehicleNumber;
        private String agentAvailabilityStatus;
    }

    /**
     * RoutePerformanceDTO for performance metrics
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoutePerformanceDTO {
        private Long performanceId;
        private Long routeId;
        private LocalDate date;
        private Integer totalDeliveries;
        private Integer successfulDeliveries;
        private Integer failedDeliveries;
        private BigDecimal averageDeliveryTime;
        private BigDecimal totalDistanceCovered;
        private BigDecimal fuelConsumption;
        private BigDecimal customerSatisfactionScore;
        private BigDecimal onTimeDeliveryRate;
        private BigDecimal efficiencyScore;
        private BigDecimal costPerDelivery;
        private BigDecimal revenueGenerated;

        // Additional fields
        private String routeName;
        private String hubName;
    }

    /**
     * RouteBoundaryDTO for geographic boundaries
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteBoundaryDTO {
        private Long boundaryId;
        private Long routeId;
        private String boundaryName;
        private String coordinates;
        private String boundaryType;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String routeName;
    }

    /**
     * RouteSearchDTO for search parameters
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteSearchDTO {
        private String name;
        private String postalCode;
        private String neighborhood;
        private String routeType;
        private String status;
        private Long hubId;
        private String trafficPattern;
        private Integer priorityLevel;
    }

    /**
     * RouteAnalyticsDTO for analytics and reporting
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteAnalyticsDTO {
        private Long hubId;
        private String hubName;
        private LocalDate dateFrom;
        private LocalDate dateTo;
        private Integer totalRoutes;
        private Integer activeRoutes;
        private Integer totalDeliveries;
        private Integer successfulDeliveries;
        private BigDecimal overallEfficiency;
        private BigDecimal averageDeliveryTime;
        private BigDecimal totalDistance;
        private BigDecimal totalRevenue;
        private List<RoutePerformanceDTO> topPerformingRoutes;
        private List<RoutePerformanceDTO> underPerformingRoutes;
    }

    /**
     * AgentAssignmentRequestDTO for agent assignment requests
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentAssignmentRequestDTO {
        @NotNull(message = "Agent ID is required")
        private Long agentId;

        private String notes;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    /**
     * BulkRouteCreateDTO for bulk route creation
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BulkRouteCreateDTO {
        @NotEmpty(message = "Routes list cannot be empty")
        @Valid
        private List<RouteCreateDTO> routes;
    }

    /**
     * RouteOptimizationSuggestionDTO for optimization suggestions
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteOptimizationSuggestionDTO {
        private Long routeId;
        private String routeName;
        private String suggestionType; // EFFICIENCY, COVERAGE, AGENT_ALLOCATION, TIMING
        private String title;
        private String description;
        private String priority; // HIGH, MEDIUM, LOW
        private Double potentialImprovement; // Percentage
        private List<String> actionItems;
        private Double estimatedCostSaving;
        private Integer estimatedTimeReduction; // in minutes
    }

    /**
     * PostalCodeValidationDTO for postal code validation
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostalCodeValidationDTO {
        private String postalCode;
        private Boolean isValid;
        private Boolean isAvailable; // Not already assigned to another route
        private String existingRouteId;
        private String existingRouteName;
        private String district;
        private String city;
    }

    /**
     * AgentDTO for agent information in routes
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentDTO {
        private Long agentId;
        private Long hubId;
        private Long userId;
        private String vehicleType;
        private String vehicleNumber;
        private String availabilityStatus;
        private Double trustScore;
        private Integer deliveryTime;
        private Integer numberOfDelivery;
        private String firstName;
        private String lastName;
        private String name;
        private String email;
        private String phoneNumber;
    }

    /**
     * StatusUpdateRequest for status updates
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusUpdateRequest {
        @NotBlank(message = "Status is required")
        private String status;
    }

    /**
     * BulkStatusUpdateRequest for bulk status updates
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BulkStatusUpdateRequest {
        @NotEmpty(message = "Route IDs list cannot be empty")
        private List<Long> routeIds;

        @NotBlank(message = "Status is required")
        private String status;
    }

    /**
     * MultipleAgentAssignmentRequest for assigning multiple agents
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MultipleAgentAssignmentRequest {
        @NotEmpty(message = "Agent IDs list cannot be empty")
        private List<Long> agentIds;
    }

    /**
     * RouteBoundaryUpdateRequest for updating route boundaries
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteBoundaryUpdateRequest {
        private String boundaryCoordinates;
        private String boundaryName;
        private String boundaryType;
        private Boolean isActive;
    }

    /**
     * RouteStatsDTO for basic route statistics
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteStatsDTO {
        private Long routeId;
        private String routeName;
        private Integer totalDeliveries;
        private Integer successfulDeliveries;
        private Integer pendingDeliveries;
        private Integer assignedAgents;
        private BigDecimal averageDeliveryTime;
        private BigDecimal efficiencyScore;
        private LocalDate lastUpdated;
    }

    /**
     * NearbyRoutesRequest for finding nearby routes
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NearbyRoutesRequest {
        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
        private Double latitude;

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
        private Double longitude;

        @DecimalMin(value = "0.1", message = "Radius must be greater than 0")
        @DecimalMax(value = "100.0", message = "Radius must be less than 100 km")
        private Double radiusKm = 5.0;
    }

    /**
     * RouteExportDTO for exporting route data
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteExportDTO {
        private Long hubId;
        private String status;
        private String routeType;
        private LocalDate dateFrom;
        private LocalDate dateTo;
        private String format; // CSV, EXCEL, PDF
        private List<String> fields;
    }

    /**
     * RouteImportDTO for importing route data
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteImportDTO {
        @NotNull(message = "Hub ID is required")
        private Long hubId;

        private Boolean skipValidation = false;
        private Boolean updateExisting = false;
        private String defaultStatus = "ACTIVE";
        private List<String> requiredFields;
    }

    /**
     * RouteValidationResultDTO for validation results
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteValidationResultDTO {
        private Boolean isValid;
        private List<String> errors;
        private List<String> warnings;
        private Integer validRoutes;
        private Integer invalidRoutes;
        private List<RouteCreateDTO> validatedRoutes;
    }
}