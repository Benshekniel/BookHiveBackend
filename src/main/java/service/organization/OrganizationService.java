package service.organization;

import model.dto.organization.OrganizationProfileDTO;
import model.dto.organization.OrganizationUpdateDTO;
import model.dto.organization.PasswordChangeDTO;
import model.dto.organization.ImageUploadResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface OrganizationService {

    OrganizationProfileDTO getOrganizationProfile(Long id);

    OrganizationProfileDTO updateOrganizationProfile(Long id, OrganizationUpdateDTO updateDTO);

    ImageUploadResponseDTO uploadProfileImage(Long id, MultipartFile imageFile);

    void changePassword(Long id, PasswordChangeDTO passwordDTO);

    void toggleTwoFactorAuth(Long id, boolean enable);

    Map<String, Object> getOrganizationStatistics(Long id);
}