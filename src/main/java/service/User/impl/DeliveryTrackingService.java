package service.User.impl;

import model.entity.Delivery;
import model.repo.Delivery.DeliveryRepository; // Use your existing repo
import service.Delivery.impl.DeliveryService; // Use your existing service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryTrackingService {

    @Autowired
    private DeliveryRepository deliveryRepository; // Your existing repo

    @Autowired
    private DeliveryService deliveryService; // Your existing service

    public List<TrackingInfo> getTrackingInfoByTransactionId(Long transactionId) {
        try {
            List<Delivery> deliveries = deliveryRepository.findByTransactionId(transactionId);

            if (deliveries.isEmpty()) {
                return generateDefaultTrackingInfo();
            }

            // Use the first delivery if multiple exist
            return generateTrackingInfoFromDelivery(deliveries.get(0));
        } catch (Exception e) {
            System.err.println("Error getting delivery by transaction ID: " + e.getMessage());
            return generateDefaultTrackingInfo();
        }
    }

    public List<TrackingInfo> getTrackingInfoByTrackingNumber(String trackingNumber) {
        try {
            Optional<Delivery> deliveryOpt = deliveryRepository.findByTrackingNumber(trackingNumber);

            if (deliveryOpt.isEmpty()) {
                return generateDefaultTrackingInfo();
            }

            return generateTrackingInfoFromDelivery(deliveryOpt.get());
        } catch (Exception e) {
            System.err.println("Error getting delivery by tracking number: " + e.getMessage());
            return generateDefaultTrackingInfo();
        }
    }

    private List<TrackingInfo> generateTrackingInfoFromDelivery(Delivery delivery) {
        List<TrackingInfo> trackingList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");

        // Always show order placed
        trackingList.add(new TrackingInfo(
                "Order Placed",
                delivery.getCreatedAt().format(formatter),
                "Your order has been confirmed and is being prepared"
        ));

        // Show status-based tracking using your existing delivery statuses
        switch (delivery.getStatus()) {
            case PLACED:
                trackingList.add(new TrackingInfo(
                        "Processing",
                        delivery.getCreatedAt().format(formatter),
                        "Order is being prepared for pickup"
                ));
                break;

            case PICKED_UP_FROM_HOME:
                if (delivery.getPickupTime() != null) {
                    trackingList.add(new TrackingInfo(
                            "Picked Up from Home",
                            delivery.getPickupTime().format(formatter),
                            "Package has been picked up from your location"
                    ));
                }
                break;

            case IN_TRANSIT_TO_LOCAL_HUB:
                trackingList.add(new TrackingInfo(
                        "In Transit to Hub",
                        delivery.getPickupTime() != null ?
                                delivery.getPickupTime().format(formatter) :
                                delivery.getCreatedAt().plusHours(1).format(formatter),
                        "Package is being transported to the local hub"
                ));
                break;

            case AT_LOCAL_HUB:
                trackingList.add(new TrackingInfo(
                        "At Local Hub",
                        delivery.getCreatedAt().plusHours(2).format(formatter),
                        "Package has arrived at the local hub and is being processed"
                ));
                break;

            case IN_TRANSIT_TO_MAIN_HUB:
                trackingList.add(new TrackingInfo(
                        "In Transit to Main Hub",
                        delivery.getCreatedAt().plusHours(3).format(formatter),
                        "Package is being transferred to the main sorting facility"
                ));
                break;

            case AT_MAIN_HUB:
                trackingList.add(new TrackingInfo(
                        "At Main Hub",
                        delivery.getCreatedAt().plusHours(6).format(formatter),
                        "Package is at the main hub being sorted for final delivery"
                ));
                break;

            case IN_TRANSIT_TO_PROPER_LOCAL_HUB:
                trackingList.add(new TrackingInfo(
                        "In Transit to Delivery Hub",
                        delivery.getCreatedAt().plusHours(8).format(formatter),
                        "Package is being transferred to the delivery hub"
                ));
                break;

            case PENDING:
                trackingList.add(new TrackingInfo(
                        "Awaiting Assignment",
                        delivery.getCreatedAt().plusHours(12).format(formatter),
                        "Package is at the delivery hub, awaiting agent assignment"
                ));
                break;

            case ASSIGNED:
                trackingList.add(new TrackingInfo(
                        "Assigned for Delivery",
                        delivery.getCreatedAt().plusHours(12).format(formatter),
                        "Package has been assigned to a delivery agent"
                ));
                break;

            case PICKED_UP:
                if (delivery.getPickupTime() != null) {
                    trackingList.add(new TrackingInfo(
                            "Out for Delivery",
                            delivery.getPickupTime().format(formatter),
                            "Package is out for delivery and will arrive soon"
                    ));
                }
                break;

            case IN_TRANSIT:
                trackingList.add(new TrackingInfo(
                        "In Transit",
                        delivery.getPickupTime() != null ?
                                delivery.getPickupTime().format(formatter) :
                                delivery.getCreatedAt().plusHours(14).format(formatter),
                        "Package is on its way to your delivery address"
                ));
                break;

            case DELIVERED:
                // Show the complete journey
                addCompleteDeliveryJourney(trackingList, delivery, formatter);
                if (delivery.getDeliveryTime() != null) {
                    trackingList.add(new TrackingInfo(
                            "Delivered",
                            delivery.getDeliveryTime().format(formatter),
                            "Package has been successfully delivered"
                    ));
                }
                break;

            case FAILED:
                trackingList.add(new TrackingInfo(
                        "Delivery Failed",
                        delivery.getCreatedAt().plusHours(16).format(formatter),
                        "Delivery attempt failed. Our team will contact you to reschedule"
                ));
                break;

            case CANCELLED:
                trackingList.add(new TrackingInfo(
                        "Cancelled",
                        delivery.getCreatedAt().plusMinutes(30).format(formatter),
                        "Order has been cancelled as requested"
                ));
                break;

            case DELAYED:
                trackingList.add(new TrackingInfo(
                        "Delayed",
                        delivery.getCreatedAt().plusHours(12).format(formatter),
                        "Delivery is delayed due to unforeseen circumstances. We apologize for the inconvenience"
                ));
                break;
        }

        return trackingList;
    }

    private void addCompleteDeliveryJourney(List<TrackingInfo> trackingList, Delivery delivery, DateTimeFormatter formatter) {
        // Add intermediate steps for delivered packages
        if (delivery.getPickupTime() != null) {
            trackingList.add(new TrackingInfo(
                    "Out for Delivery",
                    delivery.getPickupTime().format(formatter),
                    "Package was out for delivery"
            ));
        } else {
            trackingList.add(new TrackingInfo(
                    "Out for Delivery",
                    delivery.getCreatedAt().plusHours(14).format(formatter),
                    "Package was out for delivery"
            ));
        }
    }

    private List<TrackingInfo> generateDefaultTrackingInfo() {
        List<TrackingInfo> trackingList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        String currentTime = java.time.LocalDateTime.now().format(formatter);

        trackingList.add(new TrackingInfo(
                "Order Placed",
                currentTime,
                "Your order has been confirmed"
        ));

        trackingList.add(new TrackingInfo(
                "Processing",
                currentTime,
                "Order is being prepared"
        ));

        return trackingList;
    }

    // Inner class for tracking information
    public static class TrackingInfo {
        private String status;
        private String timestamp;
        private String description;

        public TrackingInfo(String status, String timestamp, String description) {
            this.status = status;
            this.timestamp = timestamp;
            this.description = description;
        }

        // Getters
        public String getStatus() { return status; }
        public String getTimestamp() { return timestamp; }
        public String getDescription() { return description; }

        // Setters
        public void setStatus(String status) { this.status = status; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public void setDescription(String description) { this.description = description; }
    }
}