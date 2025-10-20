package service.Delivery.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.entity.Route;
import model.repo.Delivery.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for automatic route assignment based on addresses
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RouteAssignmentService {

    private final RouteRepository routeRepository;

    // Postal code patterns for different countries/regions
    private static final Pattern POSTAL_CODE_PATTERN = Pattern.compile("\\b\\d{3,6}\\b");
    private static final Pattern AREA_PATTERN = Pattern.compile("\\b[A-Za-z\\s]{2,20}\\b");

    /**
     * Assign route based on delivery address
     */
    public Long assignRouteByDeliveryAddress(String deliveryAddress, Long hubId) {
        log.info("Assigning route for delivery address: {} in hub: {}", deliveryAddress, hubId);

        if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
            log.warn("Empty delivery address provided");
            return null;
        }

        return assignRouteByAddress(deliveryAddress, hubId, "DELIVERY");
    }

    /**
     * Assign pickup route based on pickup address
     */
    public Long assignPickupRouteByAddress(String pickupAddress, Long hubId) {
        log.info("Assigning pickup route for address: {} in hub: {}", pickupAddress, hubId);

        if (pickupAddress == null || pickupAddress.trim().isEmpty()) {
            log.warn("Empty pickup address provided");
            return null;
        }

        return assignRouteByAddress(pickupAddress, hubId, "PICKUP");
    }

    /**
     * Core route assignment logic for any address
     */
    private Long assignRouteByAddress(String address, Long hubId, String addressType) {
        try {
            // Strategy 1: Try postal code matching
            Long routeId = assignByPostalCode(address, hubId);
            if (routeId != null) {
                log.info("Route {} assigned via postal code for {} address: {}", routeId, addressType, address);
                return routeId;
            }

            // Strategy 2: Try neighborhood/area matching
            routeId = assignByNeighborhood(address, hubId);
            if (routeId != null) {
                log.info("Route {} assigned via neighborhood for {} address: {}", routeId, addressType, address);
                return routeId;
            }

            // Strategy 3: Try area pattern matching
            routeId = assignByAreaPattern(address, hubId);
            if (routeId != null) {
                log.info("Route {} assigned via area pattern for {} address: {}", routeId, addressType, address);
                return routeId;
            }

            // Strategy 4: Fallback to default route for hub
            routeId = assignDefaultRoute(hubId);
            if (routeId != null) {
                log.info("Default route {} assigned for {} address: {}", routeId, addressType, address);
                return routeId;
            }

            log.warn("No route could be assigned for {} address: {} in hub: {}", addressType, address, hubId);
            return null;

        } catch (Exception e) {
            log.error("Error assigning route for {} address: {} - {}", addressType, address, e.getMessage());
            return null;
        }
    }

    /**
     * Assign route based on postal code extracted from address
     */
    private Long assignByPostalCode(String address, Long hubId) {
        try {
            String postalCode = extractPostalCode(address);
            if (postalCode != null) {
                log.debug("Extracted postal code: {} from address: {}", postalCode, address);

                List<Route> routes = routeRepository.findActiveRoutesByPostalCode(hubId, postalCode);
                if (!routes.isEmpty()) {
                    return routes.get(0).getRouteId();
                }
            }
        } catch (Exception e) {
            log.warn("Error in postal code assignment: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Assign route based on neighborhood name in address
     */
    private Long assignByNeighborhood(String address, Long hubId) {
        try {
            List<String> neighborhoods = extractNeighborhoods(address);
            for (String neighborhood : neighborhoods) {
                log.debug("Checking neighborhood: {} for address: {}", neighborhood, address);

                List<Route> routes = routeRepository.findRoutesByNeighborhood(hubId, neighborhood);
                if (!routes.isEmpty()) {
                    return routes.get(0).getRouteId();
                }
            }
        } catch (Exception e) {
            log.warn("Error in neighborhood assignment: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Assign route based on area pattern matching
     */
    private Long assignByAreaPattern(String address, Long hubId) {
        try {
            List<String> areaPatterns = extractAreaPatterns(address);
            for (String pattern : areaPatterns) {
                log.debug("Checking area pattern: {} for address: {}", pattern, address);

                List<Route> routes = routeRepository.findRoutesByAreaPattern(hubId, pattern);
                if (!routes.isEmpty()) {
                    return routes.get(0).getRouteId();
                }
            }
        } catch (Exception e) {
            log.warn("Error in area pattern assignment: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Get default route for hub (highest priority, active route)
     */
    private Long assignDefaultRoute(Long hubId) {
        try {
            List<Route> routes = routeRepository.findByHubIdAndPriorityLevelOrderByPriorityLevel(hubId, 1);
            if (!routes.isEmpty()) {
                return routes.get(0).getRouteId();
            }

            // Fallback to any active route
            List<Route> activeRoutes = routeRepository.findByHubIdAndStatus(hubId, Route.RouteStatus.ACTIVE);
            if (!activeRoutes.isEmpty()) {
                return activeRoutes.get(0).getRouteId();
            }
        } catch (Exception e) {
            log.warn("Error getting default route: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Extract postal code from address
     */
    private String extractPostalCode(String address) {
        if (address == null) return null;

        Matcher matcher = POSTAL_CODE_PATTERN.matcher(address);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * Extract potential neighborhood names from address
     */
    private List<String> extractNeighborhoods(String address) {
        if (address == null) return List.of();

        // Split address by common delimiters and extract potential neighborhood names
        String[] parts = address.split("[,;\\n\\r]+");
        return List.of(parts).stream()
                .map(String::trim)
                .filter(part -> part.length() > 2 && part.length() < 50)
                .filter(part -> !part.matches(".*\\d{3,}.*")) // Exclude parts with long numbers
                .limit(3) // Limit to first 3 potential neighborhoods
                .toList();
    }

    /**
     * Extract area patterns from address for broader matching
     */
    private List<String> extractAreaPatterns(String address) {
        if (address == null) return List.of();

        Matcher matcher = AREA_PATTERN.matcher(address);
        List<String> patterns = new java.util.ArrayList<>();

        while (matcher.find() && patterns.size() < 5) {
            String pattern = matcher.group().trim();
            if (pattern.length() > 2) {
                patterns.add(pattern);
            }
        }

        return patterns;
    }

    /**
     * Get route assignment statistics for monitoring
     */
    public RouteAssignmentStats getAssignmentStats(Long hubId) {
        RouteAssignmentStats stats = new RouteAssignmentStats();
        stats.setHubId(hubId);
        stats.setTotalRoutes(routeRepository.countByHubId(hubId));
        stats.setActiveRoutes(routeRepository.countByHubIdAndStatus(hubId, Route.RouteStatus.ACTIVE));
        stats.setRoutesWithBoundaries(routeRepository.countRoutesWithBoundaries(hubId));
        return stats;
    }

    /**
     * Validate if a route can handle the assignment
     */
    public boolean canAssignToRoute(Long routeId, String addressType) {
        try {
            Optional<Route> routeOpt = routeRepository.findById(routeId);
            if (routeOpt.isPresent()) {
                Route route = routeOpt.get();
                return route.getStatus() == Route.RouteStatus.ACTIVE;
            }
        } catch (Exception e) {
            log.warn("Error validating route assignment: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Statistics class for route assignment monitoring
     */
    public static class RouteAssignmentStats {
        private Long hubId;
        private Long totalRoutes;
        private Long activeRoutes;
        private Long routesWithBoundaries;

        // Getters and setters
        public Long getHubId() { return hubId; }
        public void setHubId(Long hubId) { this.hubId = hubId; }

        public Long getTotalRoutes() { return totalRoutes; }
        public void setTotalRoutes(Long totalRoutes) { this.totalRoutes = totalRoutes; }

        public Long getActiveRoutes() { return activeRoutes; }
        public void setActiveRoutes(Long activeRoutes) { this.activeRoutes = activeRoutes; }

        public Long getRoutesWithBoundaries() { return routesWithBoundaries; }
        public void setRoutesWithBoundaries(Long routesWithBoundaries) { this.routesWithBoundaries = routesWithBoundaries; }
    }
}
