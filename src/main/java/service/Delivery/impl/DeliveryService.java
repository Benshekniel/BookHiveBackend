package service.Delivery.impl;

import model.entity.Agent;
import model.entity.Hub;
import model.entity.Route;
import model.entity.Delivery;
import model.entity.RouteAssignment;
import model.entity.Transaction;
import model.dto.Delivery.DeliveryDto.*;
import model.repo.Delivery.DeliveryRepository;
import model.repo.Delivery.AgentRepository;
import model.repo.Delivery.HubRepository;
import model.repo.Delivery.RouteRepository;
import model.repo.Delivery.TransactionRepository;
import model.repo.AllUsersRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.UUID;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AgentRepository agentRepository;
    private final HubRepository hubRepository;
    private final RouteRepository routeRepository;
    private final TransactionRepository transactionRepository;
    private final AllUsersRepo allUsersRepo;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${google.maps.api.key:AIzaSyC_N6VhUsq0bX8FEDfanh3Af-I1Bx5caFU}")
    private String googleMapsApiKey;

    public DeliveryResponseDto createDelivery(DeliveryCreateDto createDto) {
        // Validate required entities exist
        validateCreateDeliveryRequest(createDto);

        Transaction transaction = transactionRepository.findById(createDto.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + createDto.getTransactionId()));

        Hub hub = hubRepository.findById(createDto.getHubId())
                .orElseThrow(() -> new RuntimeException("Hub not found with ID: " + createDto.getHubId()));

        // Create delivery with initial values
        Delivery delivery = buildInitialDelivery(createDto, transaction);

        // Save delivery first with routeId = null
        Delivery savedDelivery = deliveryRepository.save(delivery);
        log.info("Created delivery with ID: {} and tracking number: {}",
                savedDelivery.getDeliveryId(), savedDelivery.getTrackingNumber());

        // Attempt automatic route assignment
        RouteAssignmentResult assignmentResult = assignRouteToDelivery(
                savedDelivery.getDeliveryAddress(),
                createDto.getHubId()
        );

        // Update delivery with route assignment result
        updateDeliveryWithRouteAssignment(savedDelivery, assignmentResult);

        // Save updated delivery
        savedDelivery = deliveryRepository.save(savedDelivery);

        // Convert to response DTO with route information
        return convertToResponseDto(savedDelivery, assignmentResult);
    }

    private void validateCreateDeliveryRequest(DeliveryCreateDto createDto) {
        if (createDto.getPickupAddress() == null || createDto.getPickupAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Pickup address cannot be empty");
        }
        if (createDto.getDeliveryAddress() == null || createDto.getDeliveryAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery address cannot be empty");
        }
        if (createDto.getHubId() == null || createDto.getHubId() <= 0) {
            throw new IllegalArgumentException("Valid hub ID is required");
        }
        if (createDto.getTransactionId() == null || createDto.getTransactionId() <= 0) {
            throw new IllegalArgumentException("Valid transaction ID is required");
        }
        if (createDto.getUserId() == null || createDto.getUserId() <= 0) {
            throw new IllegalArgumentException("Valid user ID is required");
        }
    }

    private Delivery buildInitialDelivery(DeliveryCreateDto createDto, Transaction transaction) {
        Delivery delivery = new Delivery();
        delivery.setTransactionId(transaction.getTransactionId());
        delivery.setUserId(createDto.getUserId());
        delivery.setPickupAddress(cleanAddress(createDto.getPickupAddress()));
        delivery.setDeliveryAddress(cleanAddress(createDto.getDeliveryAddress()));
        delivery.setStatus(Delivery.DeliveryStatus.PENDING);
        delivery.setTrackingNumber(generateTrackingNumber());
        delivery.setCreatedAt(LocalDateTime.now());
        delivery.setRouteId(null); // Will be assigned automatically

        // Set optional fields
        if (createDto.getWeight() != null) {
            delivery.setWeight(createDto.getWeight());
        }
        if (createDto.getDimensions() != null) {
            delivery.setDimensions(createDto.getDimensions());
        }
        if (createDto.getDeliveryNotes() != null) {
            delivery.setDeliveryNotes(createDto.getDeliveryNotes());
        }
        if (createDto.getPaymentMethod() != null) {
            try {
                delivery.setPaymentMethod(Delivery.PaymentMethod.valueOf(createDto.getPaymentMethod().toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid payment method: {}, defaulting to COD", createDto.getPaymentMethod());
                delivery.setPaymentMethod(Delivery.PaymentMethod.COD);
            }
        }

        return delivery;
    }

    private String cleanAddress(String address) {
        if (address == null) return null;
        return address.trim().replaceAll("\\s+", " ");
    }

    private RouteAssignmentResult assignRouteToDelivery(String deliveryAddress, Long hubId) {
        RouteAssignmentResult result = new RouteAssignmentResult();
        result.setDeliveryAddress(deliveryAddress);
        result.setHubId(hubId);

        try {
            log.info("Starting route assignment for address: {} in hub: {}", deliveryAddress, hubId);

            // Step 1: Try geocoding and polygon matching
            result = tryPolygonMatching(result);
            if (result.isSuccess()) {
                return result;
            }

            // Step 2: Try postal code matching
            result = tryPostalCodeMatching(result);
            if (result.isSuccess()) {
                return result;
            }

            // Step 3: Try distance-based matching
            result = tryDistanceMatching(result);
            if (result.isSuccess()) {
                return result;
            }

            // No match found
            result.setStatus("NO_MATCH");
            result.setMessage("No suitable route found for the delivery address");
            result.setMethod("NONE");
            log.warn("No route assignment found for address: {}", deliveryAddress);

            return result;

        } catch (Exception e) {
            log.error("Error in route assignment for address '{}': {}", deliveryAddress, e.getMessage(), e);
            result.setStatus("FAILED");
            result.setMessage("Route assignment failed: " + e.getMessage());
            result.setMethod("ERROR");
            return result;
        }
    }

    private RouteAssignmentResult tryPolygonMatching(RouteAssignmentResult result) {
        try {
            // Geocode the address
            GeocodeResult geocodeResult = geocodeAddress(result.getDeliveryAddress());
            if (geocodeResult == null) {
                result.setStatus("GEOCODING_FAILED");
                result.setMessage("Failed to geocode delivery address");
                return result;
            }

            result.setLatitude(geocodeResult.getLatitude());
            result.setLongitude(geocodeResult.getLongitude());

            log.info("Geocoded address '{}' to coordinates: {}, {}",
                    result.getDeliveryAddress(), geocodeResult.getLatitude(), geocodeResult.getLongitude());

            // Get routes with boundary coordinates
            List<Route> routes = routeRepository.findRoutesWithBoundariesByHub(result.getHubId(), Route.RouteStatus.ACTIVE);
            log.info("Found {} routes with boundaries for hub {}", routes.size(), result.getHubId());

            // Check polygon matching
            for (Route route : routes) {
                try {
                    List<Point> polygon = parseBoundaryCoordinates(route.getBoundaryCoordinates());
                    if (isPointInPolygon(geocodeResult.getLatitude(), geocodeResult.getLongitude(), polygon)) {
                        result.setRouteId(route.getRouteId());
                        result.setRouteName(route.getName());
                        result.setStatus("SUCCESS");
                        result.setMessage("Route assigned using polygon boundary matching");
                        result.setMethod("POLYGON_MATCH");
                        log.info("Found matching route '{}' (ID: {}) using polygon matching",
                                route.getName(), route.getRouteId());
                        return result;
                    }
                } catch (Exception e) {
                    log.error("Error checking polygon for route {}: {}", route.getRouteId(), e.getMessage());
                }
            }

            result.setStatus("NO_POLYGON_MATCH");
            result.setMessage("No route polygon contains the delivery coordinates");
            return result;

        } catch (Exception e) {
            log.error("Error in polygon matching: {}", e.getMessage());
            result.setStatus("POLYGON_MATCH_FAILED");
            result.setMessage("Polygon matching failed: " + e.getMessage());
            return result;
        }
    }

    private RouteAssignmentResult tryPostalCodeMatching(RouteAssignmentResult result) {
        try {
            // Extract postal code from address
            String postalCode = extractPostalCode(result.getDeliveryAddress());
            if (postalCode == null) {
                result.setStatus("NO_POSTAL_CODE");
                result.setMessage("No postal code found in delivery address");
                return result;
            }

            log.info("Extracted postal code '{}' from address", postalCode);

            // Find routes with matching postal code
            List<Route> routes = routeRepository.findActiveRoutesByPostalCode(result.getHubId(), postalCode);
            if (!routes.isEmpty()) {
                Route selectedRoute = routes.get(0); // Pick first match, could add priority logic
                result.setRouteId(selectedRoute.getRouteId());
                result.setRouteName(selectedRoute.getName());
                result.setStatus("SUCCESS");
                result.setMessage("Route assigned using postal code matching");
                result.setMethod("POSTAL_CODE");
                log.info("Found matching route '{}' (ID: {}) using postal code '{}'",
                        selectedRoute.getName(), selectedRoute.getRouteId(), postalCode);
                return result;
            }

            result.setStatus("NO_POSTAL_MATCH");
            result.setMessage("No route found for postal code: " + postalCode);
            return result;

        } catch (Exception e) {
            log.error("Error in postal code matching: {}", e.getMessage());
            result.setStatus("POSTAL_MATCH_FAILED");
            result.setMessage("Postal code matching failed: " + e.getMessage());
            return result;
        }
    }

    private RouteAssignmentResult tryDistanceMatching(RouteAssignmentResult result) {
        try {
            if (result.getLatitude() == null || result.getLongitude() == null) {
                result.setStatus("NO_COORDINATES");
                result.setMessage("No coordinates available for distance matching");
                return result;
            }

            // Find closest routes within reasonable radius (5km)
            List<Route> nearbyRoutes = routeRepository.findRoutesWithinRadius(
                    result.getHubId(), result.getLatitude(), result.getLongitude(), 5.0);

            if (!nearbyRoutes.isEmpty()) {
                Route closestRoute = nearbyRoutes.get(0); // First is closest due to ORDER BY in query
                result.setRouteId(closestRoute.getRouteId());
                result.setRouteName(closestRoute.getName());
                result.setStatus("SUCCESS");
                result.setMessage("Route assigned using distance-based matching (closest route within 5km)");
                result.setMethod("DISTANCE");
                log.info("Found closest route '{}' (ID: {}) using distance matching",
                        closestRoute.getName(), closestRoute.getRouteId());
                return result;
            }

            result.setStatus("NO_DISTANCE_MATCH");
            result.setMessage("No routes found within 5km radius");
            return result;

        } catch (Exception e) {
            log.error("Error in distance matching: {}", e.getMessage());
            result.setStatus("DISTANCE_MATCH_FAILED");
            result.setMessage("Distance matching failed: " + e.getMessage());
            return result;
        }
    }

    private void updateDeliveryWithRouteAssignment(Delivery delivery, RouteAssignmentResult result) {
        if (result.isSuccess()) {
            delivery.setRouteId(result.getRouteId());
            log.info("Assigned route ID {} to delivery {}", result.getRouteId(), delivery.getDeliveryId());
        } else {
            log.warn("Route assignment failed for delivery {}: {}", delivery.getDeliveryId(), result.getMessage());
        }
    }

    // Geocoding and utility methods...
    private GeocodeResult geocodeAddress(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String url = String.format(
                    "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                    encodedAddress, googleMapsApiKey
            );

            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);

            String status = jsonNode.get("status").asText();
            if (!"OK".equals(status)) {
                log.error("Geocoding API returned status: {} for address: {}", status, address);
                return null;
            }

            JsonNode results = jsonNode.get("results");
            if (results.isArray() && results.size() > 0) {
                JsonNode location = results.get(0).get("geometry").get("location");
                double lat = location.get("lat").asDouble();
                double lng = location.get("lng").asDouble();
                return new GeocodeResult(lat, lng);
            }

            return null;
        } catch (Exception e) {
            log.error("Error geocoding address '{}': {}", address, e.getMessage());
            return null;
        }
    }

    private List<Point> parseBoundaryCoordinates(String boundaryCoordinatesJson) {
        try {
            JsonNode coordinates = objectMapper.readTree(boundaryCoordinatesJson);
            return objectMapper.convertValue(coordinates,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Point.class));
        } catch (Exception e) {
            log.error("Error parsing boundary coordinates: {}", e.getMessage());
            throw new RuntimeException("Invalid boundary coordinates format");
        }
    }

    private boolean isPointInPolygon(double lat, double lng, List<Point> polygon) {
        if (polygon.size() < 3) {
            return false;
        }

        boolean inside = false;
        int j = polygon.size() - 1;

        for (int i = 0; i < polygon.size(); i++) {
            Point pi = polygon.get(i);
            Point pj = polygon.get(j);

            if (((pi.getLat() > lat) != (pj.getLat() > lat)) &&
                    (lng < (pj.getLng() - pi.getLng()) * (lat - pi.getLat()) / (pj.getLat() - pi.getLat()) + pi.getLng())) {
                inside = !inside;
            }
            j = i;
        }

        return inside;
    }

    private String extractPostalCode(String address) {
        // Sri Lankan postal code pattern: 5 digits
        Pattern postalCodePattern = Pattern.compile("\\b\\d{5}\\b");
        java.util.regex.Matcher matcher = postalCodePattern.matcher(address);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private String generateTrackingNumber() {
        return "TRK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private DeliveryResponseDto convertToResponseDto(Delivery delivery, RouteAssignmentResult assignmentResult) {
        DeliveryResponseDto dto = new DeliveryResponseDto();

        // Basic delivery info
        dto.setDeliveryId(delivery.getDeliveryId());
        dto.setTransactionId(delivery.getTransactionId());
        dto.setUserId(delivery.getUserId());
        dto.setRouteId(delivery.getRouteId());
        dto.setPickupAddress(delivery.getPickupAddress());
        dto.setDeliveryAddress(delivery.getDeliveryAddress());
        dto.setStatus(delivery.getStatus());
        dto.setPickupTime(delivery.getPickupTime());
        dto.setDeliveryTime(delivery.getDeliveryTime());
        dto.setTrackingNumber(delivery.getTrackingNumber());
        dto.setCreatedAt(delivery.getCreatedAt());

        // Route assignment info
        if (assignmentResult != null) {
            dto.setRouteName(assignmentResult.getRouteName());
            dto.setRouteAssignmentStatus(assignmentResult.getStatus());
            dto.setRouteAssignmentMessage(assignmentResult.getMessage());
            dto.setAssignmentMethod(assignmentResult.getMethod());
            dto.setDeliveryLatitude(assignmentResult.getLatitude());
            dto.setDeliveryLongitude(assignmentResult.getLongitude());
        }

        // Additional delivery fields
        dto.setValue(delivery.getFormattedValue());
        dto.setWeight(delivery.getWeight() != null ? delivery.getWeight() : "N/A");
        dto.setDimensions(delivery.getDimensions() != null ? delivery.getDimensions() : "N/A");
        dto.setPaymentMethod(delivery.getPaymentMethod() != null ? delivery.getPaymentMethod().name() : "N/A");
        dto.setDeliveryNotes(delivery.getDeliveryNotes() != null ? delivery.getDeliveryNotes() : "");

        // Set default values for missing related data
        dto.setAgentName("Unassigned");
        dto.setCustomerName("Customer");
        dto.setHubName("Hub");

        return dto;
    }

    // Helper classes
    private static class GeocodeResult {
        private final double latitude;
        private final double longitude;

        public GeocodeResult(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
    }

    private static class Point {
        private double lat;
        private double lng;

        public Point() {}
        public Point(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() { return lat; }
        public void setLat(double lat) { this.lat = lat; }
        public double getLng() { return lng; }
        public void setLng(double lng) { this.lng = lng; }
    }

    private static class RouteAssignmentResult {
        private String deliveryAddress;
        private Long hubId;
        private Long routeId;
        private String routeName;
        private Double latitude;
        private Double longitude;
        private String status;
        private String message;
        private String method;

        public boolean isSuccess() {
            return "SUCCESS".equals(status);
        }

        // Getters and setters
        public String getDeliveryAddress() { return deliveryAddress; }
        public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
        public Long getHubId() { return hubId; }
        public void setHubId(Long hubId) { this.hubId = hubId; }
        public Long getRouteId() { return routeId; }
        public void setRouteId(Long routeId) { this.routeId = routeId; }
        public String getRouteName() { return routeName; }
        public void setRouteName(String routeName) { this.routeName = routeName; }
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
    }

    // Rest of the existing methods...
    public List<DeliveryResponseDto> getAllDeliveries() {
        List<Object[]> results = deliveryRepository.findAllDeliveriesWithAllDetails();
        return results.stream()
                .map(this::convertToResponseDtoWithAllDetails)
                .collect(Collectors.toList());
    }

    public List<DeliveryResponseDto> getDeliveriesByHub(Long hubId) {
        return deliveryRepository.findByHubId(hubId).stream()
                .map(delivery -> convertToResponseDto(delivery, null))
                .collect(Collectors.toList());
    }

    public List<DeliveryResponseDto> getDeliveriesByAgent(Long agentId) {
        return deliveryRepository.findByAgentId(agentId).stream()
                .map(delivery -> convertToResponseDto(delivery, null))
                .collect(Collectors.toList());
    }

    public List<DeliveryResponseDto> getDeliveriesByStatus(Delivery.DeliveryStatus status) {
        return deliveryRepository.findByStatus(status).stream()
                .map(delivery -> convertToResponseDto(delivery, null))
                .collect(Collectors.toList());
    }

    public Optional<DeliveryResponseDto> getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .map(delivery -> convertToResponseDto(delivery, null));
    }

    public Optional<DeliveryResponseDto> getDeliveryByTrackingNumber(String trackingNumber) {
        return deliveryRepository.findByTrackingNumber(trackingNumber)
                .map(delivery -> convertToResponseDto(delivery, null));
    }

    public DeliveryResponseDto updateDeliveryStatus(Long deliveryId, Delivery.DeliveryStatus newStatus) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        delivery.setStatus(newStatus);

        switch (newStatus) {
            case PICKED_UP:
                delivery.setPickupTime(LocalDateTime.now());
                break;
            case DELIVERED:
                delivery.setDeliveryTime(LocalDateTime.now());
                break;
        }

        Delivery updatedDelivery = deliveryRepository.save(delivery);
        return convertToResponseDto(updatedDelivery, null);
    }

    public DeliveryResponseDto assignAgent(Long deliveryId, Long agentId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        delivery.setStatus(Delivery.DeliveryStatus.ASSIGNED);
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        return convertToResponseDto(updatedDelivery, null);
    }

    public List<DeliveryStatsDto> getDeliveryStats() {
        List<Object[]> stats = deliveryRepository.getDeliveryStatsByStatus();
        return stats.stream()
                .map(stat -> new DeliveryStatsDto(
                        (Delivery.DeliveryStatus) stat[0],
                        ((Number) stat[1]).longValue()
                ))
                .collect(Collectors.toList());
    }

    public List<DeliveryResponseDto> getTodaysDeliveries() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        return deliveryRepository.findByCreatedAtBetween(startOfDay, endOfDay).stream()
                .map(delivery -> convertToResponseDto(delivery, null))
                .collect(Collectors.toList());
    }

    public void deleteDelivery(Long deliveryId) {
        if (!deliveryRepository.existsById(deliveryId)) {
            throw new RuntimeException("Delivery not found");
        }
        deliveryRepository.deleteById(deliveryId);
    }

    private DeliveryResponseDto convertToResponseDtoWithAllDetails(Object[] result) {
        DeliveryResponseDto dto = new DeliveryResponseDto();

        Delivery delivery = (Delivery) result[0];
        String hubName = result[1] != null ? result[1].toString() : "Unknown Hub";
        String agentName = result[2] != null ? result[2].toString() : "Unassigned";
        String agentEmail = result[3] != null ? result[3].toString() : "N/A";
        String agentPhone = result[4] != null ? result[4].toString() : "N/A";
        Long transactionId = result[5] != null ? ((Number) result[5]).longValue() : null;
        String bookTitle = result[6] != null ? result[6].toString() : "Unknown Book";
        String customerName = result[7] != null ? result[7].toString() : "Unknown Customer";
        String customerEmail = result[8] != null ? result[8].toString() : "N/A";

        // Basic delivery info
        dto.setDeliveryId(delivery.getDeliveryId());
        dto.setTransactionId(delivery.getTransactionId());
        dto.setUserId(delivery.getUserId());
        dto.setRouteId(delivery.getRouteId());
        dto.setPickupAddress(delivery.getPickupAddress());
        dto.setDeliveryAddress(delivery.getDeliveryAddress());
        dto.setStatus(delivery.getStatus());
        dto.setPickupTime(delivery.getPickupTime());
        dto.setDeliveryTime(delivery.getDeliveryTime());
        dto.setTrackingNumber(delivery.getTrackingNumber());
        dto.setCreatedAt(delivery.getCreatedAt());

        // Related entity details
        dto.setHubName(hubName);
        dto.setAgentName(agentName);
        dto.setAgentEmail(agentEmail);
        dto.setAgentPhone(agentPhone);
        dto.setBookTitle(bookTitle);
        dto.setBookAuthor("Unknown Author");
        dto.setCustomerName(customerName);
        dto.setCustomerEmail(customerEmail);
        dto.setCustomerPhone("N/A");

        // Additional fields
        dto.setValue(delivery.getFormattedValue());
        dto.setPriority("normal");
        dto.setDescription("Book delivery: " + bookTitle);
        dto.setWeight(delivery.getWeight() != null ? delivery.getWeight() : "N/A");
        dto.setDimensions(delivery.getDimensions() != null ? delivery.getDimensions() : "N/A");
        dto.setPaymentMethod(delivery.getPaymentMethod() != null ? delivery.getPaymentMethod().name() : "N/A");
        dto.setDeliveryNotes(delivery.getDeliveryNotes() != null ? delivery.getDeliveryNotes() : "");
        dto.setEstimatedDelivery(delivery.getDeliveryTime());

        return dto;
    }
}