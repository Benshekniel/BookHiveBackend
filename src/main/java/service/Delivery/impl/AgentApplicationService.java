package service.Delivery.impl;

import model.entity.AgentApplication;
import model.entity.AllUsers;
import model.entity.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import model.repo.Delivery.AgentApplicationRepository;
import model.repo.AllUsersRepo;
import model.repo.Delivery.AgentRepository;
import service.Email.EmailService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class AgentApplicationService {

    @Autowired
    private AgentApplicationRepository applicationRepository;

    @Autowired
    private AllUsersRepo userRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private EmailService emailService;

    public List<AgentApplication> getAllPendingApplications() {
        return applicationRepository.findByStatus(AgentApplication.ApplicationStatus.PENDING);
    }

    public List<AgentApplication> getAllApplications() {
        return applicationRepository.findAllByOrderByAppliedDateDesc();
    }

    public List<AgentApplication> getRejectedApplications() {
        return applicationRepository.findByStatusOrderByProcessedDateDesc(AgentApplication.ApplicationStatus.REJECTED);
    }

    public List<AgentApplication> getApprovedApplications() {
        return applicationRepository.findByStatusOrderByProcessedDateDesc(AgentApplication.ApplicationStatus.APPROVED);
    }

    public List<AgentApplication> getApplicationsByStatus(AgentApplication.ApplicationStatus status, int page, int size) {
        switch (status) {
            case PENDING:
                return getAllPendingApplications();
            case APPROVED:
                return getApprovedApplications();
            case REJECTED:
                return getRejectedApplications();
            default:
                return getAllApplications();
        }
    }

    public Optional<AgentApplication> getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId);
    }

    public AgentApplication saveApplication(AgentApplication application) {
        application.setUpdatedAt(LocalDateTime.now());
        return applicationRepository.save(application);
    }

    @Transactional
    public boolean approveApplication(Long applicationId, Long approvedBy) {
        try {
            System.out.println("Starting approval process for application ID: " + applicationId);

            // Validate application exists
            Optional<AgentApplication> optApp = applicationRepository.findById(applicationId);
            if (optApp.isEmpty()) {
                throw new RuntimeException("Application not found with ID: " + applicationId);
            }

            AgentApplication application = optApp.get();
            System.out.println("Found application for: " + application.getEmail());

            // Check if application is still pending
            if (application.getStatus() != AgentApplication.ApplicationStatus.PENDING) {
                throw new RuntimeException("Application has already been processed. Current status: " + application.getStatus());
            }

            // Check if user already exists with this email
            AllUsers existingUser = userRepository.findByEmail(application.getEmail());
            if (existingUser != null) {
                throw new RuntimeException("User already exists with email: " + application.getEmail());
            }

            // Check if agent already exists for this application
            if (agentRepository.existsByApplicationId(application.getApplicationId())) {
                throw new RuntimeException("Agent already created for this application ID: " + application.getApplicationId());
            }

            // Generate temporary password
            String tempPassword = generateTemporaryPassword();
            System.out.println("Generated temporary password: " + tempPassword);

            // Create user account in AllUsers table
            AllUsers newUser = new AllUsers();
            newUser.setName(application.getFirstName() + " " + application.getLastName());
            newUser.setEmail(application.getEmail());
            newUser.setPassword(tempPassword); // In production, hash this password
            newUser.setRole("agent");
            newUser.setStatus(AllUsers.Status.active);

            System.out.println("Saving new user...");
            AllUsers savedUser = userRepository.save(newUser);
            System.out.println("User created with ID: " + savedUser.getUser_id());

            // Create agent profile in Agent table
            Agent newAgent = new Agent();

            // IMPORTANT: Handle the type conversion from int to Long
            Long convertedUserId = Long.valueOf(savedUser.getUser_id());
            newAgent.setUserId(convertedUserId);
            System.out.println("Set agent userId to: " + convertedUserId);

            newAgent.setHubId(application.getHubId());

            // Safely convert vehicle type
            try {
                Agent.VehicleType vehicleType = Agent.VehicleType.valueOf(application.getVehicleType().toUpperCase());
                newAgent.setVehicleType(vehicleType);
                System.out.println("Set vehicle type to: " + vehicleType);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid vehicle type: " + application.getVehicleType());
            }

            newAgent.setVehicleNumber(application.getVehicleRegistration());
            newAgent.setAvailabilityStatus(Agent.AvailabilityStatus.AVAILABLE);
            newAgent.setPhoneNumber(application.getPhoneNumber());
            newAgent.setTrustScore(5.0); // Starting trust score
            newAgent.setNumberOfDelivery(0);
            newAgent.setApplicationId(application.getApplicationId());
            newAgent.setAddress(application.getAddress() + ", " + application.getCity() + ", " + application.getState());
            newAgent.setCity(application.getCity());
            newAgent.setState(application.getState());
            newAgent.setJoinedDate(LocalDateTime.now());
            newAgent.setCreatedAt(LocalDateTime.now());
            newAgent.setUpdatedAt(LocalDateTime.now());

            // Clear any existing Hibernate session to ensure clean save
            System.out.println("Saving new agent...");
            Agent savedAgent = agentRepository.save(newAgent);
            System.out.println("Agent created successfully with auto-generated ID: " + savedAgent.getAgentId());

            // Update application status
            application.setStatus(AgentApplication.ApplicationStatus.APPROVED);
            application.setProcessedDate(LocalDateTime.now());
            application.setProcessedBy(approvedBy);
            application.setUpdatedAt(LocalDateTime.now());
            applicationRepository.save(application);
            System.out.println("Application status updated to APPROVED");

            // Send approval email
            sendApprovalEmail(application, tempPassword);
            System.out.println("Approval email sent");

            return true;
        } catch (Exception e) {
            System.err.println("Error in approveApplication: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to approve application: " + e.getMessage());
        }
    }

    @Transactional
    public boolean rejectApplication(Long applicationId, String rejectionReason, Long rejectedBy) {
        try {
            Optional<AgentApplication> optApp = applicationRepository.findById(applicationId);
            if (optApp.isEmpty()) {
                throw new RuntimeException("Application not found with ID: " + applicationId);
            }

            AgentApplication application = optApp.get();

            // Check if application is still pending
            if (application.getStatus() != AgentApplication.ApplicationStatus.PENDING) {
                throw new RuntimeException("Application has already been processed. Current status: " + application.getStatus());
            }

            // Update application with rejection details
            application.setStatus(AgentApplication.ApplicationStatus.REJECTED);
            application.setRejectionReason(rejectionReason);
            application.setProcessedDate(LocalDateTime.now());
            application.setProcessedBy(rejectedBy);
            application.setUpdatedAt(LocalDateTime.now());
            applicationRepository.save(application);

            // Send rejection email
            sendRejectionEmail(application, rejectionReason);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to reject application: " + e.getMessage());
        }
    }

    // Application statistics
    public ApplicationStats getApplicationStats() {
        long totalApplications = applicationRepository.count();
        long pendingApplications = applicationRepository.countByStatus(AgentApplication.ApplicationStatus.PENDING);
        long approvedApplications = applicationRepository.countByStatus(AgentApplication.ApplicationStatus.APPROVED);
        long rejectedApplications = applicationRepository.countByStatus(AgentApplication.ApplicationStatus.REJECTED);

        return new ApplicationStats(totalApplications, pendingApplications, approvedApplications, rejectedApplications);
    }

    // Helper class for statistics
    public static class ApplicationStats {
        private final long total;
        private final long pending;
        private final long approved;
        private final long rejected;

        public ApplicationStats(long total, long pending, long approved, long rejected) {
            this.total = total;
            this.pending = pending;
            this.approved = approved;
            this.rejected = rejected;
        }

        // Getters
        public long getTotal() { return total; }
        public long getPending() { return pending; }
        public long getApproved() { return approved; }
        public long getRejected() { return rejected; }
    }

    private void sendApprovalEmail(AgentApplication application, String tempPassword) {
        try {
            String subject = "Application Approved - Welcome to Our Delivery Team!";
            String body = String.format(
                    "Dear %s,\n\n" +
                            "Congratulations! Your delivery agent application has been approved.\n\n" +
                            "You can now download our mobile app and login with:\n" +
                            "Email: %s\n" +
                            "Temporary Password: %s\n\n" +
                            "Please change your password after first login.\n\n" +
                            "Application Details:\n" +
                            "- Application ID: %s\n" +
                            "- Vehicle Type: %s\n" +
                            "- Processed Date: %s\n\n" +
                            "Welcome to the team!\n\n" +
                            "Best regards,\n" +
                            "Delivery Management Team",
                    application.getFirstName() + " " + application.getLastName(),
                    application.getEmail(),
                    tempPassword,
                    application.getApplicationId(),
                    application.getVehicleType(),
                    application.getProcessedDate()
            );

            emailService.sendEmail(
                    application.getEmail(),
                    subject,
                    body,
                    application.getFirstName() + " " + application.getLastName(),
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRejectionEmail(AgentApplication application, String rejectionReason) {
        try {
            String subject = "Application Status Update";
            String body = String.format(
                    "Dear %s,\n\n" +
                            "Thank you for your interest in joining our delivery team. " +
                            "After careful review, we regret to inform you that your application " +
                            "cannot be approved at this time.\n\n" +
                            "Application Details:\n" +
                            "- Application ID: %s\n" +
                            "- Submitted Date: %s\n" +
                            "- Processed Date: %s\n\n" +
                            "Reason for Rejection:\n%s\n\n" +
                            "You may reapply after addressing the mentioned concerns. " +
                            "Please ensure all documents are clear and meet our requirements.\n\n" +
                            "If you have any questions, please contact our support team.\n\n" +
                            "Best regards,\n" +
                            "Delivery Management Team",
                    application.getFirstName() + " " + application.getLastName(),
                    application.getApplicationId(),
                    application.getAppliedDate(),
                    application.getProcessedDate(),
                    rejectionReason
            );

            emailService.sendEmail(
                    application.getEmail(),
                    subject,
                    body,
                    application.getFirstName() + " " + application.getLastName(),
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
}