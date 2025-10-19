//package service.organization.impl;
//
//import model.dto.organization.OrganizationProfileDTO;
//import model.dto.organization.OrganizationUpdateDTO;
//import model.dto.organization.PasswordChangeDTO;
//import model.dto.organization.ImageUploadResponseDTO;
//import model.entity.Organization;
//import model.repo.organization.OrganizationRepository;
//import model.repo.organization.BookRequestRepository;
//import model.repo.organization.DonationRepository;
//import model.repo.organization.FeedbackRepository;
//import service.GoogleDriveUpload.FileStorageService;
//import service.organization.OrganizationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class OrganizationServiceImpl implements OrganizationService {
//
//    private final OrganizationRepository organizationRepository;
//    private final BookRequestRepository bookRequestRepository;
//    private final DonationRepository donationRepository;
//    private final FeedbackRepository feedbackRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final FileStorageService fileStorageService;
//
//    @Autowired
//    public OrganizationServiceImpl(
//            OrganizationRepository organizationRepository,
//            BookRequestRepository bookRequestRepository,
//            DonationRepository donationRepository,
//            FeedbackRepository feedbackRepository,
//            PasswordEncoder passwordEncoder,
//            FileStorageService fileStorageService) {
//        this.organizationRepository = organizationRepository;
//        this.bookRequestRepository = bookRequestRepository;
//        this.donationRepository = donationRepository;
//        this.feedbackRepository = feedbackRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.fileStorageService = fileStorageService;
//    }
//
//    @Override
//    public OrganizationProfileDTO getOrganizationProfile(Long id) {
//        Organization organization = findOrganizationById(id);
//        return mapToProfileDTO(organization);
//    }
//
//    @Override
//    public OrganizationProfileDTO updateOrganizationProfile(Long id, OrganizationUpdateDTO updateDTO) {
//        Organization organization = findOrganizationById(id);
//
//        // Update fields
//        organization.setOrganizationName(updateDTO.getOrganizationName());
//        organization.setRegistrationNumber(updateDTO.getRegistrationNumber());
//        organization.setEmail(updateDTO.getEmail());
//        organization.setPhone(updateDTO.getPhone());
//        organization.setAddress(updateDTO.getAddress());
//        organization.setDescription(updateDTO.getDescription());
//        organization.setWebsite(updateDTO.getWebsite());
//        organization.setEstablished(updateDTO.getEstablished());
//        organization.setStudentCount(updateDTO.getStudentCount());
//        organization.setContactPerson(updateDTO.getContactPerson());
//        organization.setContactTitle(updateDTO.getContactTitle());
//        organization.setOrganizationType(updateDTO.getOrganizationType());
//        organization.setPublicProfile(updateDTO.getPublicProfile());
//        organization.setContactPermissions(updateDTO.getContactPermissions());
//        organization.setActivityVisibility(updateDTO.getActivityVisibility());
//
//        Organization saved = organizationRepository.save(organization);
//        return mapToProfileDTO(saved);
//    }
//
//    @Override
//    public ImageUploadResponseDTO uploadProfileImage(Long id, MultipartFile imageFile) {
//        Organization organization = findOrganizationById(id);
//
//        if (imageFile.isEmpty()) {
//            throw new BadRequestException("Please select a file to upload");
//        }
//
//        // Handle file upload
//        String fileName = fileStorageService.storeFile(imageFile, "organization_profiles");
//        String fileUrl = fileStorageService.getFileUrl(fileName);
//
//        // Update organization profile
//        organization.setProfileImage(fileUrl);
//        organizationRepository.save(organization);
//
//        return new ImageUploadResponseDTO(fileUrl);
//    }
//
//    @Override
//    public void changePassword(Long id, PasswordChangeDTO passwordDTO) {
//        Organization organization = findOrganizationById(id);
//
//        // Verify current password
//        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), organization.getPassword())) {
//            throw new BadRequestException("Current password is incorrect");
//        }
//
//        // Verify new password matches confirmation
//        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
//            throw new BadRequestException("New password and confirmation do not match");
//        }
//
//        // Update password
//        organization.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
//        organizationRepository.save(organization);
//    }
//
//    @Override
//    public void toggleTwoFactorAuth(Long id, boolean enable) {
//        Organization organization = findOrganizationById(id);
//        organization.setTwoFactorEnabled(enable);
//        organizationRepository.save(organization);
//    }
//
//    @Override
//    public Map<String, Object> getOrganizationStatistics(Long id) {
//        // Ensure organization exists
//        findOrganizationById(id);
//
//        // Get statistics from repositories
//        Map<String, Object> stats = new HashMap<>();
//        stats.put("totalRequests", bookRequestRepository.countByOrganizationId(id));
//        stats.put("totalDonationsReceived", donationRepository.countByOrganizationIdAndStatus(id, "RECEIVED"));
//        stats.put("averageFeedbackRating", feedbackRepository.getAverageRatingByOrganizationId(id));
//
//        return stats;
//    }
//
//    private Organization findOrganizationById(Long id) {
//        return organizationRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
//    }
//
//    private OrganizationProfileDTO mapToProfileDTO(Organization organization) {
//        OrganizationProfileDTO dto = new OrganizationProfileDTO();
//        dto.setId(organization.getId());
//        dto.setOrganizationName(organization.getOrganizationName());
//        dto.setRegistrationNumber(organization.getRegistrationNumber());
//        dto.setEmail(organization.getEmail());
//        dto.setPhone(organization.getPhone());
//        dto.setAddress(organization.getAddress());
//        dto.setDescription(organization.getDescription());
//        dto.setWebsite(organization.getWebsite());
//        dto.setEstablished(organization.getEstablished());
//        dto.setStudentCount(organization.getStudentCount());
//        dto.setContactPerson(organization.getContactPerson());
//        dto.setContactTitle(organization.getContactTitle());
//        dto.setOrganizationType(organization.getOrganizationType());
//        dto.setProfileImage(organization.getProfileImage());
//        dto.setPublicProfile(organization.getPublicProfile());
//        dto.setContactPermissions(organization.getContactPermissions());
//        dto.setActivityVisibility(organization.getActivityVisibility());
//        dto.setTwoFactorEnabled(organization.getTwoFactorEnabled());
//        dto.setCreatedAt(organization.getCreatedAt());
//        dto.setUpdatedAt(organization.getUpdatedAt());
//        return dto;
//    }
//}