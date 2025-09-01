package controller;

import model.dto.organization.OrganizationProfileDTO;
import model.dto.organization.OrganizationProfileUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.organization.OrganizationService;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

	@Autowired
	private OrganizationService organizationService;

	@GetMapping("/{orgId}")
	public OrganizationProfileDTO getProfile(@PathVariable Long orgId) {
		return organizationService.getOrganizationProfile(orgId);
	}

	@PutMapping("/{orgId}")
	public OrganizationProfileDTO updateProfile(@PathVariable Long orgId, @RequestBody OrganizationProfileUpdateDTO updateDTO) {
		return organizationService.updateOrganizationProfile(orgId, updateDTO);
	}

	@PostMapping
	public OrganizationProfileDTO createProfile(@RequestBody OrganizationProfileUpdateDTO createDTO) {
		return organizationService.createOrganizationProfile(createDTO);
	}
}
