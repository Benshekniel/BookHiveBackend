// 1. OrganizationController.java
package controller.organization;

import model.dto.Organization.OrganizationDto.*;
import service.organization.impl.OrganizationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationServiceImpl organizationService;

    @GetMapping("/{orgId}")
    public ResponseEntity<OrganizationResponseDto> getOrganizationProfile(@PathVariable Long orgId) {
        return organizationService.getOrganizationProfile(orgId)
                .map(org -> ResponseEntity.ok(org))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{orgId}")
    public ResponseEntity<OrganizationResponseDto> updateOrganizationProfile(
            @PathVariable Long orgId,
            @RequestBody OrganizationUpdateDto updateDto) {
        try {
            OrganizationResponseDto updatedOrg = organizationService.updateOrganizationProfile(orgId, updateDto);
            return ResponseEntity.ok(updatedOrg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{orgId}/statistics")
    public ResponseEntity<OrganizationStatisticsDto> getOrganizationStatistics(@PathVariable Long orgId) {
        return organizationService.getOrganizationStatistics(orgId)
                .map(stats -> ResponseEntity.ok(stats))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{orgId}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponseDto> uploadOrganizationImage(
            @PathVariable Long orgId,
            @RequestParam("image") MultipartFile imageFile) {
        try {
            ImageUploadResponseDto response = organizationService.uploadOrganizationImage(orgId, imageFile);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{orgId}/change-password")
    public ResponseEntity<PasswordChangeResponseDto> changePassword(
            @PathVariable Long orgId,
            @RequestBody PasswordChangeRequestDto passwordData) {
        try {
            PasswordChangeResponseDto response = organizationService.changePassword(orgId, passwordData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{orgId}/two-factor-auth")
    public ResponseEntity<TwoFactorAuthResponseDto> toggleTwoFactorAuth(
            @PathVariable Long orgId,
            @RequestBody TwoFactorAuthRequestDto requestDto) {
        try {
            TwoFactorAuthResponseDto response = organizationService.toggleTwoFactorAuth(
                    orgId, requestDto.getEnable());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}