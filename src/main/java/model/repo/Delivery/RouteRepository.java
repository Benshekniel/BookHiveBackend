package model.repo.Delivery;

import model.entity.Agent;
import model.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Fixed RouteRepository interface for basic Route entity operations
 * Handles only Route operations without nested entities
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    @Query("SELECT a FROM Agent a JOIN AllUsers u ON a.userId = u.user_id WHERE a.agentId IN " +
            "(SELECT ra.agentId FROM RouteAssignment ra WHERE ra.routeId = :routeId AND ra.status = 'ACTIVE')")
    List<Agent> findByRouteId(@Param("routeId") Long routeId);
    // Existing methods remain unchanged...

    // ================================
    // ASSIGNMENT QUERIES
    // ================================

    @Query("SELECT ra.agentId FROM RouteAssignment ra WHERE ra.routeId = :routeId AND ra.status = 'ACTIVE'")
    List<Long> findAgentIdsByRouteId(@Param("routeId") Long routeId);

    // ================================
    // ROUTE ASSIGNMENT METHODS
    // ================================

    /**
     * Find routes with boundary coordinates for geocoding and polygon matching
     */
    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = :status AND " +
            "r.boundaryCoordinates IS NOT NULL AND r.boundaryCoordinates != '' AND TRIM(r.boundaryCoordinates) != ''")
    List<Route> findRoutesWithBoundariesByHub(@Param("hubId") Long hubId, @Param("status") Route.RouteStatus status);

    /**
     * Find all routes with boundary coordinates (regardless of hub)
     */
    @Query("SELECT r FROM Route r WHERE r.boundaryCoordinates IS NOT NULL AND r.boundaryCoordinates != '' AND TRIM(r.boundaryCoordinates) != ''")
    List<Route> findAllRoutesWithBoundaries();

    /**
     * Find routes that contain a specific postal code
     */
    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE' AND " +
            "r.postalCodes IS NOT NULL AND r.postalCodes ILIKE %:postalCode%")
    List<Route> findActiveRoutesByPostalCode(@Param("hubId") Long hubId, @Param("postalCode") String postalCode);

    /**
     * Find the closest route by center coordinates
     */
    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE' AND " +
            "r.centerLatitude IS NOT NULL AND r.centerLongitude IS NOT NULL " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(r.centerLatitude)) * " +
            "cos(radians(r.centerLongitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(r.centerLatitude)))) ASC")
    List<Route> findClosestRoutesByCoordinates(@Param("hubId") Long hubId,
                                               @Param("latitude") Double latitude,
                                               @Param("longitude") Double longitude);

    /**
     * Check if coordinates fall within route center bounds (approximate check)
     */
    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE' AND " +
            "r.centerLatitude IS NOT NULL AND r.centerLongitude IS NOT NULL AND " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(r.centerLatitude)) * " +
            "cos(radians(r.centerLongitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(r.centerLatitude)))) <= :radiusKm")
    List<Route> findRoutesWithinRadius(@Param("hubId") Long hubId,
                                       @Param("latitude") Double latitude,
                                       @Param("longitude") Double longitude,
                                       @Param("radiusKm") Double radiusKm);

    /**
     * Find routes by neighborhood that might contain the address
     */
    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE' AND " +
            "r.neighborhoods IS NOT NULL AND r.neighborhoods ILIKE %:neighborhood%")
    List<Route> findRoutesByNeighborhood(@Param("hubId") Long hubId, @Param("neighborhood") String neighborhood);

    /**
     * Find routes that cover a specific area pattern
     */
    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE' AND " +
            "(r.coverageArea ILIKE %:areaPattern% OR r.neighborhoods ILIKE %:areaPattern% OR r.landmarks ILIKE %:areaPattern%)")
    List<Route> findRoutesByAreaPattern(@Param("hubId") Long hubId, @Param("areaPattern") String areaPattern);

    /**
     * Count routes with boundary coordinates
     */
    @Query("SELECT COUNT(r) FROM Route r WHERE r.hubId = :hubId AND " +
            "r.boundaryCoordinates IS NOT NULL AND r.boundaryCoordinates != ''")
    Long countRoutesWithBoundaries(@Param("hubId") Long hubId);

    /**
     * Find routes that need boundary coordinate updates
     */
    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE' AND " +
            "(r.boundaryCoordinates IS NULL OR r.boundaryCoordinates = '' OR TRIM(r.boundaryCoordinates) = '')")
    List<Route> findRoutesNeedingBoundaries(@Param("hubId") Long hubId);

    /**
     * Update boundary coordinates for a route
     */
    @Modifying
    @Transactional
    @Query("UPDATE Route r SET r.boundaryCoordinates = :boundaryCoordinates, r.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE r.routeId = :routeId")
    int updateBoundaryCoordinates(@Param("routeId") Long routeId, @Param("boundaryCoordinates") String boundaryCoordinates);

    /**
     * Update center coordinates for a route
     */
    @Modifying
    @Transactional
    @Query("UPDATE Route r SET r.centerLatitude = :latitude, r.centerLongitude = :longitude, r.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE r.routeId = :routeId")
    int updateCenterCoordinates(@Param("routeId") Long routeId, @Param("latitude") Double latitude, @Param("longitude") Double longitude);

    // Existing methods continue...
    List<Route> findByHubId(Long hubId);
    List<Route> findByStatus(Route.RouteStatus status);
    List<Route> findByRouteType(Route.RouteType routeType);
    List<Route> findByTrafficPattern(Route.TrafficPattern trafficPattern);
    List<Route> findByHubIdAndStatus(Long hubId, Route.RouteStatus status);

    @Query("SELECT r FROM Route r WHERE r.name ILIKE %:name%")
    List<Route> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT r FROM Route r WHERE r.postalCodes ILIKE %:postalCode%")
    List<Route> findByPostalCode(@Param("postalCode") String postalCode);

    @Query("SELECT r FROM Route r WHERE r.neighborhoods ILIKE %:neighborhood%")
    List<Route> findByNeighborhood(@Param("neighborhood") String neighborhood);

    @Query("SELECT r FROM Route r WHERE " +
            "(:name IS NULL OR r.name ILIKE %:name%) AND " +
            "(:postalCode IS NULL OR r.postalCodes ILIKE %:postalCode%) AND " +
            "(:neighborhood IS NULL OR r.neighborhoods ILIKE %:neighborhood%) AND " +
            "(:routeType IS NULL OR r.routeType = :routeType) AND " +
            "(:status IS NULL OR r.status = :status) AND " +
            "(:hubId IS NULL OR r.hubId = :hubId) AND " +
            "(:trafficPattern IS NULL OR r.trafficPattern = :trafficPattern) AND " +
            "(:priorityLevel IS NULL OR r.priorityLevel = :priorityLevel)")
    List<Route> searchRoutes(@Param("name") String name,
                             @Param("postalCode") String postalCode,
                             @Param("neighborhood") String neighborhood,
                             @Param("routeType") Route.RouteType routeType,
                             @Param("status") Route.RouteStatus status,
                             @Param("hubId") Long hubId,
                             @Param("trafficPattern") Route.TrafficPattern trafficPattern,
                             @Param("priorityLevel") Integer priorityLevel);

    @Query("SELECT r FROM Route r WHERE " +
            "r.centerLatitude IS NOT NULL AND r.centerLongitude IS NOT NULL AND " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(r.centerLatitude)) * " +
            "cos(radians(r.centerLongitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(r.centerLatitude)))) <= :radiusKm")
    List<Route> findNearbyRoutes(@Param("latitude") Double latitude,
                                 @Param("longitude") Double longitude,
                                 @Param("radiusKm") Double radiusKm);

    Long countByHubId(Long hubId);
    Long countByHubIdAndStatus(Long hubId, Route.RouteStatus status);

    List<Route> findByHubIdAndPriorityLevelOrderByPriorityLevel(Long hubId, Integer priorityLevel);
    List<Route> findByHubIdOrderByPriorityLevel(Long hubId);

    @Query("SELECT r FROM Route r WHERE r.postalCodes ILIKE %:postalCode%")
    List<Route> findRoutesWithPostalCode(@Param("postalCode") String postalCode);

    @Query("SELECT r.postalCodes FROM Route r WHERE r.hubId = :hubId AND r.postalCodes IS NOT NULL")
    List<String> getUsedPostalCodesByHub(@Param("hubId") Long hubId);

    @Query("SELECT r FROM Route r WHERE r.coverageArea ILIKE %:pattern%")
    List<Route> findByCoverageAreaContaining(@Param("pattern") String pattern);

    @Query("SELECT r FROM Route r WHERE r.landmarks ILIKE %:landmark%")
    List<Route> findByLandmarksContaining(@Param("landmark") String landmark);

    @Query("SELECT r FROM Route r WHERE r.vehicleRestrictions ILIKE %:restriction%")
    List<Route> findByVehicleRestrictionsContaining(@Param("restriction") String restriction);

    @Query("SELECT r FROM Route r WHERE r.estimatedDeliveryTime <= :maxTime")
    List<Route> findByEstimatedDeliveryTimeLessThanEqual(@Param("maxTime") Integer maxTime);

    @Query("SELECT r FROM Route r WHERE r.estimatedDeliveryTime BETWEEN :minTime AND :maxTime")
    List<Route> findByEstimatedDeliveryTimeBetween(@Param("minTime") Integer minTime, @Param("maxTime") Integer maxTime);

    @Query("SELECT r FROM Route r WHERE r.maxDailyDeliveries >= :minDeliveries")
    List<Route> findByMaxDailyDeliveriesGreaterThanEqual(@Param("minDeliveries") Integer minDeliveries);

    @Query("SELECT r FROM Route r WHERE r.maxDailyDeliveries BETWEEN :minDeliveries AND :maxDeliveries")
    List<Route> findByMaxDailyDeliveriesBetween(@Param("minDeliveries") Integer minDeliveries, @Param("maxDeliveries") Integer maxDeliveries);

    @Query("SELECT " +
            "COUNT(r) as totalRoutes, " +
            "COUNT(CASE WHEN r.status = 'ACTIVE' THEN 1 END) as activeRoutes, " +
            "COUNT(CASE WHEN r.status = 'INACTIVE' THEN 1 END) as inactiveRoutes, " +
            "AVG(r.estimatedDeliveryTime) as avgDeliveryTime, " +
            "AVG(r.maxDailyDeliveries) as avgMaxDeliveries, " +
            "AVG(r.priorityLevel) as avgPriority " +
            "FROM Route r WHERE r.hubId = :hubId")
    Object[] getRouteStatsByHub(@Param("hubId") Long hubId);

    @Query("SELECT r.priorityLevel, COUNT(r) " +
            "FROM Route r WHERE r.hubId = :hubId " +
            "GROUP BY r.priorityLevel ORDER BY r.priorityLevel")
    List<Object[]> getPriorityDistributionByHub(@Param("hubId") Long hubId);

    @Query("SELECT r.status, COUNT(r) " +
            "FROM Route r WHERE r.hubId = :hubId " +
            "GROUP BY r.status")
    List<Object[]> getStatusDistributionByHub(@Param("hubId") Long hubId);

//    @Query("SELECT r.routeType, COUNT(r) " +
//            "FROM Route r WHERE r.hubId = :hubId " +
//            "GROUP wedgesByType")
//    List<Object[]> getTypeDistributionByHub(@Param("hubId") Long hubId);

    @Query("SELECT r.trafficPattern, COUNT(r) " +
            "FROM Route r WHERE r.hubId = :hubId " +
            "GROUP BY r.trafficPattern")
    List<Object[]> getTrafficPatternDistributionByHub(@Param("hubId") Long hubId);

    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE' AND " +
            "(r.maxDailyDeliveries IS NULL OR r.maxDailyDeliveries < :minCapacity)")
    List<Route> findLowCapacityRoutes(@Param("hubId") Long hubId, @Param("minCapacity") Integer minCapacity);

    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE' AND " +
            "r.estimatedDeliveryTime > :threshold")
    List<Route> findHighDeliveryTimeRoutes(@Param("hubId") Long hubId, @Param("threshold") Integer threshold);

    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE' AND " +
            "r.priorityLevel = :priorityLevel")
    List<Route> findRoutesByPriorityForOptimization(@Param("hubId") Long hubId, @Param("priorityLevel") Integer priorityLevel);

    @Modifying
    @Transactional
    @Query("UPDATE Route r SET r.status = :status, r.updatedAt = CURRENT_TIMESTAMP WHERE r.routeId IN :routeIds")
    int bulkUpdateRouteStatus(@Param("routeIds") List<Long> routeIds, @Param("status") Route.RouteStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Route r SET r.priorityLevel = :priorityLevel, r.updatedAt = CURRENT_TIMESTAMP WHERE r.routeId IN :routeIds")
    int bulkUpdateRoutePriority(@Param("routeIds") List<Long> routeIds, @Param("priorityLevel") Integer priorityLevel);

    @Modifying
    @Transactional
    @Query("UPDATE Route r SET r.updatedAt = CURRENT_TIMESTAMP WHERE r.routeId = :routeId")
    void touchRouteForUpdate(@Param("routeId") Long routeId);

    @Query("SELECT r FROM Route r WHERE " +
            "r.centerLatitude BETWEEN :southLat AND :northLat AND " +
            "r.centerLongitude BETWEEN :westLng AND :eastLng")
    List<Route> findRoutesInBoundingBox(@Param("southLat") Double southLat,
                                        @Param("northLat") Double northLat,
                                        @Param("westLng") Double westLng,
                                        @Param("eastLng") Double eastLng);

    @Query("SELECT r FROM Route r WHERE r.postalCodes LIKE :regexPattern")
    List<Route> findByPostalCodePattern(@Param("regexPattern") String regexPattern);

    @Query("SELECT r FROM Route r WHERE r.createdAt BETWEEN :startDate AND :endDate")
    List<Route> findRoutesCreatedBetween(@Param("startDate") java.time.LocalDateTime startDate,
                                         @Param("endDate") java.time.LocalDateTime endDate);

    @Query("SELECT r FROM Route r WHERE r.updatedAt BETWEEN :startDate AND :endDate")
    List<Route> findRoutesUpdatedBetween(@Param("startDate") java.time.LocalDateTime startDate,
                                         @Param("endDate") java.time.LocalDateTime endDate);

    List<Route> findByCreatedBy(Long createdBy);
    List<Route> findByCreatedByAndHubId(Long createdBy, Long hubId);

    boolean existsByNameAndHubId(String name, Long hubId);

    @Query("SELECT COUNT(r) > 0 FROM Route r WHERE r.hubId = :hubId AND r.postalCodes ILIKE %:postalCode%")
    boolean existsByPostalCodeInHub(@Param("postalCode") String postalCode, @Param("hubId") Long hubId);

    @Query("SELECT COUNT(r) > 0 FROM Route r WHERE r.routeId = :routeId AND " +
            "r.centerLatitude IS NOT NULL AND r.centerLongitude IS NOT NULL")
    boolean hasCoordinates(@Param("routeId") Long routeId);

    @Query("SELECT COALESCE(SUM(r.maxDailyDeliveries), 0) FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE'")
    Long getTotalDeliveryCapacityByHub(@Param("hubId") Long hubId);

    @Query("SELECT COALESCE(AVG(r.estimatedDeliveryTime), 0) FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE'")
    Double getAverageDeliveryTimeByHub(@Param("hubId") Long hubId);

    @Query("SELECT COUNT(r) FROM Route r WHERE r.hubId = :hubId AND r.trafficPattern = :trafficPattern")
    Long getRouteCountByTrafficPattern(@Param("hubId") Long hubId, @Param("trafficPattern") Route.TrafficPattern trafficPattern);

    @Query("SELECT COUNT(r) FROM Route r WHERE r.hubId = :hubId AND r.routeType = :routeType")
    Long getRouteCountByType(@Param("hubId") Long hubId, @Param("routeType") Route.RouteType routeType);

    // ================================
    // UTILITY METHODS FOR ROUTE ASSIGNMENT
    // ================================

    /**
     * Check if a route has valid boundary coordinates
     */
    @Query("SELECT COUNT(r) > 0 FROM Route r WHERE r.routeId = :routeId AND " +
            "r.boundaryCoordinates IS NOT NULL AND r.boundaryCoordinates != '' AND TRIM(r.boundaryCoordinates) != ''")
    boolean hasBoundaryCoordinates(@Param("routeId") Long routeId);

    /**
     * Get route with its boundary coordinates for assignment
     */
    @Query("SELECT r FROM Route r WHERE r.routeId = :routeId AND " +
            "r.boundaryCoordinates IS NOT NULL AND r.boundaryCoordinates != ''")
    Route findRouteWithBoundaries(@Param("routeId") Long routeId);

    /**
     * Find routes for delivery assignment optimization
     */
    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE' " +
            "ORDER BY r.priorityLevel ASC, r.estimatedDeliveryTime ASC")
    List<Route> findOptimalRoutesForAssignment(@Param("hubId") Long hubId);

    /**
     * Get route assignment statistics
     */
    @Query("SELECT " +
            "COUNT(d) as totalDeliveries, " +
            "COUNT(CASE WHEN d.status = 'DELIVERED' THEN 1 END) as deliveredCount, " +
            "COUNT(CASE WHEN d.status = 'PENDING' THEN 1 END) as pendingCount " +
            "FROM Delivery d WHERE d.routeId = :routeId")
    Object[] getRouteAssignmentStats(@Param("routeId") Long routeId);

    /**
     * Find routes with high delivery load for load balancing
     */
    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE' AND " +
            "(SELECT COUNT(d) FROM Delivery d WHERE d.routeId = r.routeId AND d.status IN ('PENDING', 'ASSIGNED', 'PICKED_UP', 'IN_TRANSIT')) " +
            "> :threshold")
    List<Route> findOverloadedRoutes(@Param("hubId") Long hubId, @Param("threshold") Long threshold);

    /**
     * Find routes with low delivery load for load balancing
     */
    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = 'ACTIVE' AND " +
            "(SELECT COUNT(d) FROM Delivery d WHERE d.routeId = r.routeId AND d.status IN ('PENDING', 'ASSIGNED', 'PICKED_UP', 'IN_TRANSIT')) " +
            "< :threshold")
    List<Route> findUnderloadedRoutes(@Param("hubId") Long hubId, @Param("threshold") Long threshold);
}