package service.organization.impl;

import model.dto.organization.OrganizationProfileDTO;
import model.dto.organization.OrganizationProfileUpdateDTO;
import model.entity.Organization;
import model.repo.OrgRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.organization.OrganizationService;

@Service
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrgRepo orgRepo;

	private OrganizationProfileDTO mapToDTO(Organization org) {
		if (org == null) return null;
		return new OrganizationProfileDTO(
				org.getOrgId(),
				org.getFname(), // Assuming fname is organizationName
				org.getRegNo(),
				org.getEmail(),
				String.valueOf(org.getPhone()),
				org.getAddress(),
				null, // description not in entity
				null, // website not in entity
				String.valueOf(org.getYears()),
				org.getLname(), // contactPerson
				null // contactTitle not in entity
		);
	}

	@Override
	public OrganizationProfileDTO getOrganizationProfile(Long orgId) {
		Organization org = orgRepo.findById(orgId).orElse(null);
		return mapToDTO(org);
	}

	@Override
	public OrganizationProfileDTO updateOrganizationProfile(Long orgId, OrganizationProfileUpdateDTO updateDTO) {
		Organization org = orgRepo.findById(orgId).orElse(null);
		if (org == null) return null;
		org.setFname(updateDTO.getOrganizationName());
		org.setRegNo(updateDTO.getRegistrationNumber());
		org.setEmail(updateDTO.getEmail());
		try { org.setPhone(Integer.parseInt(updateDTO.getPhone())); } catch (Exception ignored) {}
		org.setAddress(updateDTO.getAddress());
		// org.setDescription(updateDTO.getDescription()); // Not in entity
		// org.setWebsite(updateDTO.getWebsite()); // Not in entity
		try { org.setYears(Integer.parseInt(updateDTO.getEstablished())); } catch (Exception ignored) {}
		org.setLname(updateDTO.getContactPerson());
		// org.setContactTitle(updateDTO.getContactTitle()); // Not in entity
		orgRepo.save(org);
		return mapToDTO(org);
	}

	@Override
	public OrganizationProfileDTO createOrganizationProfile(OrganizationProfileUpdateDTO createDTO) {
		Organization org = new Organization();
		org.setFname(createDTO.getOrganizationName());
		org.setRegNo(createDTO.getRegistrationNumber());
		org.setEmail(createDTO.getEmail());
		try { org.setPhone(Integer.parseInt(createDTO.getPhone())); } catch (Exception ignored) {}
		org.setAddress(createDTO.getAddress());
		try { org.setYears(Integer.parseInt(createDTO.getEstablished())); } catch (Exception ignored) {}
		org.setLname(createDTO.getContactPerson());
		orgRepo.save(org);
		return mapToDTO(org);
	}
}
