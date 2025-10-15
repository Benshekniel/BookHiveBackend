package controller.Delivery;

import model.entity.AgentApplication;
import service.Delivery.impl.AgentApplicationService;
import service.Delivery.impl.DocumentDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.Delivery.impl.CloudinaryService;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@CrossOrigin(origins = {"http://localhost:9999", "http://localhost:3000"})
@RestController
@RequestMapping("/api/agent-applications")
public class AgentApplicationController {

    @Autowired
    private AgentApplicationService applicationService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private DocumentDownloadService documentDownloadService;

    @GetMapping
    public ResponseEntity<List<AgentApplication>> getAllApplications() {
        List<AgentApplication> applications = applicationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<AgentApplication>> getPendingApplications() {
        List<AgentApplication> applications = applicationService.getAllPendingApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/rejected")
    public ResponseEntity<List<AgentApplication>> getRejectedApplications() {
        List<AgentApplication> applications = applicationService.getRejectedApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<AgentApplication>> getApprovedApplications() {
        List<AgentApplication> applications = applicationService.getApprovedApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{id}/documents/validate-urls")
    public ResponseEntity<Map<String, Object>> validateDocumentUrls(@PathVariable Long id) {
        try {
            AgentApplication application = applicationService.getApplicationById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            Map<String, Object> validation = new HashMap<>();

            // Check each document URL
            Map<String, String> documents = new HashMap<>();
            documents.put("idFront", application.getIdFrontUrl());
            documents.put("idBack", application.getIdBackUrl());
            documents.put("vehicleRc", application.getVehicleRcUrl());
            documents.put("profileImage", application.getProfileImageUrl());

            Map<String, Boolean> urlStatus = new HashMap<>();

            for (Map.Entry<String, String> entry : documents.entrySet()) {
                String url = entry.getValue();
                boolean isValid = false;

                if (url != null && !url.trim().isEmpty()) {
                    try {
                        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setRequestMethod("HEAD");
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);

                        int responseCode = connection.getResponseCode();
                        isValid = (responseCode == HttpURLConnection.HTTP_OK);

                        System.out.println("URL validation - " + entry.getKey() + ": " + url + " -> " + responseCode);
                    } catch (Exception e) {
                        System.err.println("Error validating URL " + url + ": " + e.getMessage());
                    }
                }

                urlStatus.put(entry.getKey(), isValid);
            }

            validation.put("urlStatus", urlStatus);
            validation.put("documents", documents);

            return ResponseEntity.ok(validation);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to validate URLs: " + e.getMessage()));
        }
    }

    /**
     * Download all documents for an application as a ZIP file
     */
    @GetMapping("/{id}/documents/download-all")
    public ResponseEntity<ByteArrayResource> downloadAllDocuments(@PathVariable Long id) {
        try {
            AgentApplication application = applicationService.getApplicationById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            Map<String, String> documentMap = new HashMap<>();
            documentMap.put("id_front", application.getIdFrontUrl());
            documentMap.put("id_back", application.getIdBackUrl());
            documentMap.put("vehicle_rc", application.getVehicleRcUrl());
            documentMap.put("profile_image", application.getProfileImageUrl());

            // Remove null or empty URLs
            documentMap.entrySet().removeIf(entry ->
                    entry.getValue() == null || entry.getValue().trim().isEmpty());

            if (documentMap.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            byte[] zipData = documentDownloadService.createZipWithDocuments(
                    documentMap,
                    application.getApplicationId().toString()
            );

            String zipFileName = String.format("application_%s_documents.zip",
                    application.getApplicationId());

            ByteArrayResource resource = new ByteArrayResource(zipData);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFileName + "\"")
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .contentLength(zipData.length)
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<AgentApplicationService.ApplicationStats> getApplicationStats() {
        AgentApplicationService.ApplicationStats stats = applicationService.getApplicationStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AgentApplication>> getApplicationsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            AgentApplication.ApplicationStatus appStatus = AgentApplication.ApplicationStatus.valueOf(status.toUpperCase());
            List<AgentApplication> applications = applicationService.getApplicationsByStatus(appStatus, page, size);
            return ResponseEntity.ok(applications);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentApplication> getApplicationById(@PathVariable Long id) {
        return applicationService.getApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AgentApplication> createApplication(@RequestBody AgentApplication application) {
        try {
            AgentApplication savedApplication = applicationService.saveApplication(application);
            return ResponseEntity.ok(savedApplication);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{id}/upload-document")
    public ResponseEntity<Map<String, String>> uploadDocument(
            @PathVariable Long id,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = cloudinaryService.uploadFile(file);

            // Update application with document URL
            AgentApplication application = applicationService.getApplicationById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            switch (documentType.toUpperCase()) {
                case "ID_FRONT":
                    application.setIdFrontUrl(fileUrl);
                    break;
                case "ID_BACK":
                    application.setIdBackUrl(fileUrl);
                    break;
                case "VEHICLE_RC":
                    application.setVehicleRcUrl(fileUrl);
                    break;
                case "PROFILE_IMAGE":
                    application.setProfileImageUrl(fileUrl);
                    break;
                default:
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "Invalid document type"));
            }

            // Check if all documents are uploaded
            if (application.getIdFrontUrl() != null &&
                    application.getIdBackUrl() != null &&
                    application.getVehicleRcUrl() != null) {
                application.setDocumentsStatus(AgentApplication.DocumentStatus.COMPLETE);
            }

            applicationService.saveApplication(application);

            return ResponseEntity.ok(Map.of("fileUrl", fileUrl, "message", "Document uploaded successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to upload document: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Map<String, String>> approveApplication(
            @PathVariable Long id,
            @RequestParam Long approvedBy) {
        try {
            boolean success = applicationService.approveApplication(id, approvedBy);
            if (success) {
                return ResponseEntity.ok(Map.of("message", "Application approved successfully"));
            } else {
                return ResponseEntity.internalServerError()
                        .body(Map.of("error", "Failed to approve application"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectApplication(
            @PathVariable Long id,
            @RequestParam String rejectionReason,
            @RequestParam Long rejectedBy) {
        try {
            if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Rejection reason is required"));
            }

            boolean success = applicationService.rejectApplication(id, rejectionReason, rejectedBy);
            if (success) {
                return ResponseEntity.ok(Map.of("message", "Application rejected successfully"));
            } else {
                return ResponseEntity.internalServerError()
                        .body(Map.of("error", "Failed to reject application"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgentApplication> updateApplication(
            @PathVariable Long id,
            @RequestBody AgentApplication applicationUpdate) {
        try {
            AgentApplication existingApplication = applicationService.getApplicationById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            // Update only allowed fields
            if (applicationUpdate.getDocumentsStatus() != null) {
                existingApplication.setDocumentsStatus(applicationUpdate.getDocumentsStatus());
            }

            AgentApplication savedApplication = applicationService.saveApplication(existingApplication);
            return ResponseEntity.ok(savedApplication);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteApplication(@PathVariable Long id) {
        try {
            AgentApplication application = applicationService.getApplicationById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            // Only allow deletion of rejected applications that are older than 30 days
            if (application.getStatus() == AgentApplication.ApplicationStatus.REJECTED) {
                // Implement deletion logic if needed
                return ResponseEntity.ok(Map.of("message", "Application deletion requested"));
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Only rejected applications can be deleted"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // NEW DOCUMENT DOWNLOAD ENDPOINTS

    /**
     * Download individual document by application ID and document type
     */
    @GetMapping("/{id}/documents/{documentType}/download")
    public ResponseEntity<ByteArrayResource> downloadDocument(
            @PathVariable Long id,
            @PathVariable String documentType) {
        try {
            AgentApplication application = applicationService.getApplicationById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            String documentUrl = getDocumentUrl(application, documentType);
            if (documentUrl == null || documentUrl.trim().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            byte[] documentData = documentDownloadService.downloadDocumentFromUrl(documentUrl);
            String fileName = documentDownloadService.generateFileName(
                    application.getApplicationId().toString(),
                    documentType,
                    documentUrl
            );

            ByteArrayResource resource = new ByteArrayResource(documentData);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType(documentDownloadService.getContentType(fileName)))
                    .contentLength(documentData.length)
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get document as base64 (alternative method for frontend)
     */
    @GetMapping("/{id}/documents/{documentType}/base64")
    public ResponseEntity<Map<String, Object>> getDocumentBase64(
            @PathVariable Long id,
            @PathVariable String documentType) {
        try {
            AgentApplication application = applicationService.getApplicationById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            String documentUrl = getDocumentUrl(application, documentType);
            if (documentUrl == null || documentUrl.trim().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            byte[] documentData = documentDownloadService.downloadDocumentFromUrl(documentUrl);
            String base64Data = java.util.Base64.getEncoder().encodeToString(documentData);

            String fileName = documentDownloadService.generateFileName(
                    application.getApplicationId().toString(),
                    documentType,
                    documentUrl
            );

            Map<String, Object> response = new HashMap<>();
            response.put("base64Data", base64Data);
            response.put("fileName", fileName);
            response.put("contentType", documentDownloadService.getContentType(fileName));
            response.put("size", documentData.length);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to get document: " + e.getMessage()));
        }
    }

    /**
     * Check if document exists and is accessible
     */
    @GetMapping("/{id}/documents/{documentType}/exists")
    public ResponseEntity<Map<String, Object>> checkDocumentExists(
            @PathVariable Long id,
            @PathVariable String documentType) {
        try {
            AgentApplication application = applicationService.getApplicationById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            String documentUrl = getDocumentUrl(application, documentType);
            boolean exists = documentUrl != null && !documentUrl.trim().isEmpty();

            Map<String, Object> response = new HashMap<>();
            response.put("exists", exists);
            response.put("url", documentUrl);
            response.put("documentType", documentType);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to check document: " + e.getMessage()));
        }
    }

    /**
     * Get document information without downloading
     */
    @GetMapping("/{id}/documents/{documentType}/info")
    public ResponseEntity<Map<String, Object>> getDocumentInfo(
            @PathVariable Long id,
            @PathVariable String documentType) {
        try {
            AgentApplication application = applicationService.getApplicationById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            String documentUrl = getDocumentUrl(application, documentType);
            if (documentUrl == null || documentUrl.trim().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            String fileName = documentDownloadService.generateFileName(
                    application.getApplicationId().toString(),
                    documentType,
                    documentUrl
            );

            Map<String, Object> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("url", documentUrl);
            response.put("contentType", documentDownloadService.getContentType(fileName));
            response.put("documentType", documentType);
            response.put("extension", documentDownloadService.getFileExtensionFromUrl(documentUrl));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to get document info: " + e.getMessage()));
        }
    }

    /**
     * Helper method to get document URL by type
     */
    private String getDocumentUrl(AgentApplication application, String documentType) {
        switch (documentType.toLowerCase()) {
            case "idfront":
            case "id_front":
                return application.getIdFrontUrl();
            case "idback":
            case "id_back":
                return application.getIdBackUrl();
            case "vehiclerc":
            case "vehicle_rc":
                return application.getVehicleRcUrl();
            case "profileimage":
            case "profile_image":
                return application.getProfileImageUrl();
            default:
                throw new IllegalArgumentException("Invalid document type: " + documentType);
        }
    }
}