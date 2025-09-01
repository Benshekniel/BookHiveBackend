package service.organization;

import model.dto.organization.OrganizationProfileDTO;
import model.dto.organization.OrganizationProfileUpdateDTO;

public interface OrganizationService {
	OrganizationProfileDTO getOrganizationProfile(Long orgId);
	OrganizationProfileDTO updateOrganizationProfile(Long orgId, OrganizationProfileUpdateDTO updateDTO);
	OrganizationProfileDTO createOrganizationProfile(OrganizationProfileUpdateDTO createDTO);
}
