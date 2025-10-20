package service.organization.impl;

import model.dto.organization.OrganizationProfileDTO;
import model.dto.organization.OrganizationUpdateDTO;
import model.dto.organization.PasswordChangeDTO;
import model.dto.organization.ImageUploadResponseDTO;
import model.entity.Organization;
import model.repo.OrgRepo;
import model.repo.organization.BookRequestRepository;
import model.repo.organization.DonationRepository;
import model.repo.organization.FeedbackRepository;
import service.GoogleDriveUpload.FileStorageService;
import service.organization.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrgRepo orgRepo;
    private final BookRequestRepository bookRequestRepository;
    private final DonationRepository donationRepository;
    private final FeedbackRepository feedbackRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    @Autowired
    public OrganizationServiceImpl(
            OrgRepo orgRepo,
            BookRequestRepository bookRequestRepository,
            DonationRepository donationRepository,
            FeedbackRepository feedbackRepository,
            PasswordEncoder passwordEncoder,
            FileStorageService fileStorageService) {
        this.orgRepo = orgRepo;
        this.bookRequestRepository = bookRequestRepository;
        this.donationRepository = donationRepository;
        this.feedbackRepository = feedbackRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public OrganizationProfileDTO getOrganizationProfile(Long orgId) {
        Organization organization = findOrganizationByOrgId(orgId);
        return mapToProfileDTO(organization);
    }

    @Override
    public OrganizationProfileDTO updateOrganizationProfile(Long orgId, OrganizationUpdateDTO updateDTO) {
        Organization organization = findOrganizationByOrgId(orgId);

        organization.setType(updateDTO.getOrganizationType() != null ? updateDTO.getOrganizationType() : organization.getType());
        organization.setRegNo(updateDTO.getRegistrationNumber() != null ? updateDTO.getRegistrationNumber() : organization.getRegNo());
        organization.setFname(updateDTO.getContactPerson() != null ? updateDTO.getContactPerson() : organization.getFname());
        organization.setLname(organization.getLname());
        organization.setEmail(updateDTO.getEmail() != null ? updateDTO.getEmail() : organization.getEmail());
        organization.setAddress(updateDTO.getAddress() != null ? updateDTO.getAddress() : organization.getAddress());
        organization.setCity(organization.getCity());
        organization.setState(organization.getState());
        organization.setZip(organization.getZip());
        organization.setYears(updateDTO.getEstablished() != null ? updateDTO.getEstablished() : organization.getYears());

        Organization saved = orgRepo.save(organization);
        return mapToProfileDTO(saved);
    }

    @Override
    public ImageUploadResponseDTO uploadProfileImage(Long orgId, MultipartFile imageFile) {
        Organization organization = findOrganizationByOrgId(orgId);

        if (imageFile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please select a file to upload");
        }

        String fileName = fileStorageService.storeFile(imageFile, "organization_profiles");
        String fileUrl = fileStorageService.getFileUrl(fileName);

        organization.setImageFileName(fileName);
        organization.setFileType(imageFile.getContentType());
        orgRepo.save(organization);

        return new ImageUploadResponseDTO(fileUrl);
    }

    @Override
    public void changePassword(Long orgId, PasswordChangeDTO passwordDTO) {
        Organization organization = findOrganizationByOrgId(orgId);

        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), organization.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }

        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password and confirmation do not match");
        }

        organization.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        orgRepo.save(organization);
    }

    @Override
    public void toggleTwoFactorAuth(Long orgId, boolean enable) {
        Organization organization = findOrganizationByOrgId(orgId);
        throw new UnsupportedOperationException("Two-factor authentication not supported in current Organization entity");
    }

    @Override
    public Map<String, Object> getOrganizationStatistics(Long orgId) {
        findOrganizationByOrgId(orgId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRequests", bookRequestRepository.countByOrganizationOrgId(orgId));
        stats.put("totalDonationsReceived", donationRepository.countByOrganizationIdAndStatus(orgId, "RECEIVED"));
        stats.put("averageFeedbackRating", feedbackRepository.findAverageRatingByOrganizationId(orgId));

        return stats;
    }

    private Organization findOrganizationByOrgId(Long orgId) {
        return orgRepo.findByOrgId(orgId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found with orgId: " + orgId));
    }

    private OrganizationProfileDTO mapToProfileDTO(Organization organization) {
        OrganizationProfileDTO dto = new OrganizationProfileDTO();
        dto.setId(organization.getOrgId());
        dto.setOrganizationType(organization.getType());
        dto.setRegistrationNumber(organization.getRegNo());
        dto.setContactPerson(organization.getFname());
        dto.setEmail(organization.getEmail());
        dto.setAddress(organization.getAddress());
        dto.setEstablished(organization.getYears());
        dto.setDescription(null);
        dto.setWebsite(null);
        dto.setStudentCount(0);
        dto.setContactTitle(null);
        dto.setProfileImage(organization.getImageFileName() != null ? fileStorageService.getFileUrl(organization.getImageFileName()) : null);
        dto.setPublicProfile(false);
        dto.setContactPermissions(null);
        dto.setActivityVisibility(null);
        dto.setTwoFactorEnabled(false);
        dto.setCreatedAt(null);
        dto.setUpdatedAt(null);
        return dto;
    }
}
