package controller.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import model.dto.organization.ImageUploadResponseDTO;
import model.dto.organization.OrganizationProfileDTO;
import model.dto.organization.OrganizationUpdateDTO;
import model.dto.organization.PasswordChangeDTO;
import service.organization.OrganizationService;

import java.util.Map;
@CrossOrigin(origins = {"http://localhost:9999", "http://localhost:3000"})

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationProfileDTO> getOrganizationProfile(@PathVariable Long id) {
        OrganizationProfileDTO profile = organizationService.getOrganizationProfile(id);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationProfileDTO> updateOrganizationProfile(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationUpdateDTO updateDTO) {
        OrganizationProfileDTO updated = organizationService.updateOrganizationProfile(id, updateDTO);
        return ResponseEntity.ok(updated);
    }

    @PostMapping(value = "/{id}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponseDTO> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile imageFile) {
        ImageUploadResponseDTO response = organizationService.uploadProfileImage(id, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody PasswordChangeDTO passwordDTO) {
        organizationService.changePassword(id, passwordDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/two-factor-auth")
    public ResponseEntity<Void> toggleTwoFactorAuth(
            @PathVariable Long id,
            @RequestBody(required = false) Boolean enable) {
        organizationService.toggleTwoFactorAuth(id, enable != null ? enable : true);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<Map<String, Object>> getOrganizationStatistics(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.getOrganizationStatistics(id));
    }
}