// RouteDTO.java - Complete Enhanced Version
package model.dto.Delivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/**
 * Main RouteDTO for route data transfer with enhanced boundary coordinate support
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {
    private Long routeId;

    @NotBlank(message = "Route name is required")
    private String name;

    private String description;

    @NotNull(message = "Hub ID is required")
    private Long hubId;

    private String coverageArea;
    private String postalCodes;
    private String status;

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double centerLatitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double centerLongitude;

    // Enhanced boundary coordinates field with validation
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;

    // Calculated fields
    private Integer dailyDeliveries;
    private Integer assignedRiders;
    private Integer totalRiders;
    private Double averageDeliveryTime;
    private List<AgentDTO> agents = new ArrayList<>();
    private Double efficiency;
    private String hubName;
    private String createdByName;

    // Boundary coordinate statistics
    private Integer boundaryPointCount;
    private Double boundaryArea; // in square kilometers
    private Boolean hasBoundaryCoordinates;

    /**
     * RouteCreateDTO for creating new routes with enhanced boundary support
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteCreateDTO {
        @NotBlank(message = "Route name is required")
        @Size(min = 3, max = 100, message = "Route name must be between 3 and 100 characters")
        private String name;

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        private String description;

        @NotNull(message = "Hub ID is required")
        private Long hubId;

        @Size(max = 50, message = "Coverage area cannot exceed 50 characters")
        private String coverageArea;

        @Size(max = 200, message = "Postal codes cannot exceed 200 characters")
        private String postalCodes;

        @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
        private Double centerLatitude;

        @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
        private Double centerLongitude;

        // Enhanced boundary coordinates with validation
        private String boundaryCoordinates;

        @Min(value = 1, message = "Estimated delivery time must be greater than 0")
        @Max(value = 1440, message = "Estimated delivery time cannot exceed 24 hours")
        private Integer estimatedDeliveryTime;

        @Min(value = 1, message = "Max daily deliveries must be greater than 0")
        @Max(value = 1000, message = "Max daily deliveries cannot exceed 1000")
        private Integer maxDailyDeliveries;

        @Min(value = 1, message = "Priority level must be between 1 and 5")
        @Max(value = 5, message = "Priority level must be between 1 and 5")
        private Integer priorityLevel;

        @Size(max = 1000, message = "Neighborhoods data cannot exceed 1000 characters")
        private String neighborhoods;

        @Size(max = 1000, message = "Landmarks data cannot exceed 1000 characters")
        private String landmarks;

        @Pattern(regexp = "LOW|MODERATE|HIGH|VARIABLE", message = "Invalid traffic pattern")
        private String trafficPattern;

        @Pattern(regexp = "RESIDENTIAL|COMMERCIAL|INDUSTRIAL|MIXED|UNIVERSITY|DOWNTOWN", message = "Invalid route type")
        private String routeType;

        @Size(max = 500, message = "Vehicle restrictions cannot exceed 500 characters")
        private String vehicleRestrictions;

        private Long createdBy;

        // Boundary validation flag
        private Boolean validateBoundaries = true;
    }

    /**
     * RouteUpdateDTO for updating existing routes
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteUpdateDTO {
        @Size(min = 3, max = 100, message = "Route name must be between 3 and 100 characters")
        private String name;

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        private String description;

        @Size(max = 50, message = "Coverage area cannot exceed 50 characters")
        private String coverageArea;

        @Size(max = 200, message = "Postal codes cannot exceed 200 characters")
        private String postalCodes;

        @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
        private Double centerLatitude;

        @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
        private Double centerLongitude;

        // Enhanced boundary coordinates
        private String boundaryCoordinates;

        @Min(value = 1, message = "Estimated delivery time must be greater than 0")
        @Max(value = 1440, message = "Estimated delivery time cannot exceed 24 hours")
        private Integer estimatedDeliveryTime;

        @Min(value = 1, message = "Max daily deliveries must be greater than 0")
        @Max(value = 1000, message = "Max daily deliveries cannot exceed 1000")
        private Integer maxDailyDeliveries;

        @Min(value = 1, message = "Priority level must be between 1 and 5")
        @Max(value = 5, message = "Priority level must be between 1 and 5")
        private Integer priorityLevel;

        @Size(max = 1000, message = "Neighborhoods data cannot exceed 1000 characters")
        private String neighborhoods;

        @Size(max = 1000, message = "Landmarks data cannot exceed 1000 characters")
        private String landmarks;

        @Pattern(regexp = "LOW|MODERATE|HIGH|VARIABLE", message = "Invalid traffic pattern")
        private String trafficPattern;

        @Pattern(regexp = "RESIDENTIAL|COMMERCIAL|INDUSTRIAL|MIXED|UNIVERSITY|DOWNTOWN", message = "Invalid route type")
        private String routeType;

        @Size(max = 500, message = "Vehicle restrictions cannot exceed 500 characters")
        private String vehicleRestrictions;

        // Boundary validation flag
        private Boolean validateBoundaries = true;

        // Update metadata
        private LocalDateTime lastModified;
        private String updateReason;
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
        private LocalDate endDate;

        // Additional fields for display
        private String routeName;
        private String agentName;
        private String assignedByName;
        private String agentVehicleType;
        private String agentVehicleNumber;
        private String agentAvailabilityStatus;

        // Assignment metrics
        private Integer totalDeliveries;
        private Double averageRating;
        private String performanceStatus;
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

        // Performance trends
        private String performanceTrend; // IMPROVING, DECLINING, STABLE
        private BigDecimal weekOverWeekChange;
        private BigDecimal monthOverMonthChange;
    }

    /**
     * Enhanced RouteBoundaryDTO for geographic boundaries
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteBoundaryDTO {
        private Long boundaryId;
        private Long routeId;
        private String boundaryName;

        @NotBlank(message = "Boundary coordinates are required")
        private String coordinates;

        @Pattern(regexp = "PRIMARY|SECONDARY|BACKUP", message = "Invalid boundary type")
        private String boundaryType;

        @NotNull(message = "Active status is required")
        private Boolean isActive;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String routeName;

        // Boundary statistics
        private Integer pointCount;
        private Double areaInSquareKm;
        private Double perimeterInKm;
        private BoundaryCoordinate centerPoint;
        private BoundaryBounds bounds;

        // Validation status
        private Boolean isValid;
        private List<String> validationErrors = new ArrayList<>();
    }

    /**
     * BoundaryCoordinate for individual coordinate points
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoundaryCoordinate {
        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
        private Double lat;

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
        private Double lng;

        private Integer order; // Order in the boundary polygon
        private String label; // Optional label for the point
    }

    /**
     * BoundaryBounds for boundary bounding box
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoundaryBounds {
        private Double north;
        private Double south;
        private Double east;
        private Double west;

        public Double getLatitudeSpan() {
            return north != null && south != null ? north - south : null;
        }

        public Double getLongitudeSpan() {
            return east != null && west != null ? east - west : null;
        }
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

        // Enhanced search criteria
        private Boolean hasBoundaryCoordinates;
        private Double minArea;
        private Double maxArea;
        private Integer minDeliveries;
        private Integer maxDeliveries;
        private LocalDate createdAfter;
        private LocalDate createdBefore;

        // Geographic search
        private Double centerLat;
        private Double centerLng;
        private Double radiusKm;
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
        private Integer routesWithBoundaries;
        private Integer totalDeliveries;
        private Integer successfulDeliveries;
        private BigDecimal overallEfficiency;
        private BigDecimal averageDeliveryTime;
        private BigDecimal totalDistance;
        private BigDecimal totalRevenue;
        private BigDecimal totalCoverageArea;

        // Performance insights
        private List<RoutePerformanceDTO> topPerformingRoutes = new ArrayList<>();
        private List<RoutePerformanceDTO> underPerformingRoutes = new ArrayList<>();
        private List<RouteBoundaryStatsDTO> boundaryStatistics = new ArrayList<>();

        // Trends
        private RouteAnalyticsTrends trends;
    }

    /**
     * RouteBoundaryStatsDTO for boundary statistics
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteBoundaryStatsDTO {
        private Long routeId;
        private String routeName;
        private Boolean hasBoundary;
        private Integer boundaryPoints;
        private Double boundaryArea;
        private Double boundaryPerimeter;
        private String boundaryType;
        private LocalDateTime lastUpdated;

        // Validation status
        private Boolean isValidBoundary;
        private List<String> validationIssues = new ArrayList<>();
    }

    /**
     * RouteAnalyticsTrends for trend analysis
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteAnalyticsTrends {
        private BigDecimal deliveryGrowthRate;
        private BigDecimal efficiencyTrend;
        private BigDecimal coverageExpansion;
        private Integer newRoutesThisPeriod;
        private Integer routesWithBoundariesAdded;
        private String overallTrend; // IMPROVING, DECLINING, STABLE
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

        @Size(max = 500, message = "Notes cannot exceed 500 characters")
        private String notes;

        private LocalDate startDate;
        private LocalDate endDate;

        // Assignment preferences
        private String shiftPreference; // MORNING, AFTERNOON, EVENING, NIGHT
        private List<String> vehicleTypes = new ArrayList<>();
        private Boolean isTemporary;
        private Integer maxDailyDeliveries;
    }

    /**
     * BulkRouteCreateDTO for bulk route creation
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BulkRouteCreateDTO {
        @NotEmpty(message = "Routes list cannot be empty")
        @Size(max = 50, message = "Cannot create more than 50 routes at once")
        @Valid
        private List<RouteCreateDTO> routes = new ArrayList<>();

        // Bulk operation settings
        private Boolean validateAllBoundaries = true;
        private Boolean skipInvalidRoutes = false;
        private String bulkOperationId;
        private Long createdBy;
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

        @Pattern(regexp = "EFFICIENCY|COVERAGE|AGENT_ALLOCATION|TIMING|BOUNDARY|CAPACITY",
                message = "Invalid suggestion type")
        private String suggestionType;

        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Description is required")
        private String description;

        @Pattern(regexp = "HIGH|MEDIUM|LOW", message = "Invalid priority level")
        private String priority;

        @DecimalMin(value = "0.0", message = "Potential improvement must be positive")
        @DecimalMax(value = "100.0", message = "Potential improvement cannot exceed 100%")
        private Double potentialImprovement; // Percentage

        private List<String> actionItems = new ArrayList<>();
        private Double estimatedCostSaving;
        private Integer estimatedTimeReduction; // in minutes

        // Implementation details
        private String implementationComplexity; // EASY, MEDIUM, HARD
        private Integer estimatedImplementationDays;
        private List<String> requiredResources = new ArrayList<>();

        // Boundary-specific suggestions
        private Boolean requiresBoundaryUpdate;
        private String boundaryRecommendation;
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
        private String province;

        // Geographic validation
        private BoundaryCoordinate coordinates;
        private Boolean isWithinServiceArea;
        private Double distanceFromNearestHub;

        // Recommendations
        private String recommendedRouteId;
        private String recommendedRouteName;
        private String validationMessage;
    }

    /**
     * Enhanced AgentDTO for agent information in routes
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

        // Performance metrics
        private Double averageRating;
        private Integer completedDeliveries;
        private Integer onTimeDeliveries;
        private BigDecimal onTimePercentage;

        // Route-specific data
        private LocalDateTime assignedToRouteAt;
        private String routeAssignmentStatus;
        private Integer routeDeliveryCount;
        private String preferredRouteTypes;
    }

    /**
     * StatusUpdateRequest for status updates
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusUpdateRequest {
        @NotBlank(message = "Status is required")
        @Pattern(regexp = "ACTIVE|INACTIVE|MAINTENANCE|SUSPENDED", message = "Invalid status")
        private String status;

        @Size(max = 200, message = "Reason cannot exceed 200 characters")
        private String reason;

        private LocalDateTime effectiveDate;
        private Long updatedBy;
    }

    /**
     * BulkStatusUpdateRequest for bulk status updates
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BulkStatusUpdateRequest {
        @NotEmpty(message = "Route IDs list cannot be empty")
        @Size(max = 100, message = "Cannot update more than 100 routes at once")
        private List<Long> routeIds = new ArrayList<>();

        @NotBlank(message = "Status is required")
        @Pattern(regexp = "ACTIVE|INACTIVE|MAINTENANCE|SUSPENDED", message = "Invalid status")
        private String status;

        @Size(max = 200, message = "Reason cannot exceed 200 characters")
        private String reason;

        private Long updatedBy;
    }

    /**
     * MultipleAgentAssignmentRequest for assigning multiple agents
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MultipleAgentAssignmentRequest {
        @NotEmpty(message = "Agent IDs list cannot be empty")
        @Size(max = 20, message = "Cannot assign more than 20 agents at once")
        private List<Long> agentIds = new ArrayList<>();

        private LocalDate startDate;
        private LocalDate endDate;
        private String notes;
        private Long assignedBy;
    }

    /**
     * Enhanced RouteBoundaryUpdateRequest for updating route boundaries
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteBoundaryUpdateRequest {
        @NotBlank(message = "Boundary coordinates are required")
        private String boundaryCoordinates;

        private String boundaryName;

        @Pattern(regexp = "PRIMARY|SECONDARY|BACKUP", message = "Invalid boundary type")
        private String boundaryType;

        private Boolean isActive;

        // Update metadata
        private String updateReason;
        private Long updatedBy;
        private Boolean validateCoordinates = true;

        // Backup previous boundary
        private Boolean backupPrevious = true;
    }
}