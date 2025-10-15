package service.Delivery.impl;

import org.springframework.stereotype.Service;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class DocumentDownloadService {

    public byte[] downloadDocumentFromUrl(String documentUrl) throws IOException {
        if (documentUrl == null || documentUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Document URL is required");
        }

        // Add logging to see what URL we're trying to access
        System.out.println("Attempting to download from URL: " + documentUrl);

        HttpURLConnection connection = null;
        try {
            URL url = new URL(documentUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(30000);
            connection.setRequestProperty("User-Agent", "Java Application");

            // Add headers for Cloudinary if needed
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Cache-Control", "no-cache");

            int responseCode = connection.getResponseCode();
            System.out.println("Response code for " + documentUrl + ": " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream()) {
                    return inputStream.readAllBytes();
                }
            } else {
                // Log the error response
                String errorMessage = "HTTP Error: " + responseCode;
                if (connection.getErrorStream() != null) {
                    try (InputStream errorStream = connection.getErrorStream()) {
                        String errorBody = new String(errorStream.readAllBytes());
                        errorMessage += " - " + errorBody;
                        System.err.println("Error response body: " + errorBody);
                    }
                }
                throw new IOException(errorMessage);
            }
        } catch (Exception e) {
            System.err.println("Exception downloading from " + documentUrl + ": " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public byte[] createZipWithDocuments(Map<String, String> documentMap, String applicationId) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(baos)) {
            for (Map.Entry<String, String> entry : documentMap.entrySet()) {
                String documentType = entry.getKey();
                String documentUrl = entry.getValue();

                if (documentUrl != null && !documentUrl.trim().isEmpty()) {
                    try {
                        byte[] documentData = downloadDocumentFromUrl(documentUrl);
                        String fileName = generateFileName(applicationId, documentType, documentUrl);

                        ZipArchiveEntry zipEntry = new ZipArchiveEntry(fileName);
                        zipEntry.setSize(documentData.length);
                        zipOut.putArchiveEntry(zipEntry);
                        zipOut.write(documentData);
                        zipOut.closeArchiveEntry();

                        System.out.println("Added " + fileName + " to ZIP (" + documentData.length + " bytes)");
                    } catch (Exception e) {
                        // Log error but continue with other documents
                        System.err.println("Failed to add document " + documentType + " to ZIP: " + e.getMessage());
                    }
                }
            }
        }

        if (baos.size() == 0) {
            throw new IOException("No documents were successfully added to the ZIP file");
        }

        return baos.toByteArray();
    }

    public String getFileExtensionFromUrl(String url) {
        try {
            String[] parts = url.split("/");
            String filename = parts[parts.length - 1];
            int lastDotIndex = filename.lastIndexOf('.');
            if (lastDotIndex > 0) {
                return filename.substring(lastDotIndex + 1).toLowerCase();
            }
        } catch (Exception e) {
            // ignore
        }
        return "jpg";
    }

    public String generateFileName(String applicationId, String documentType, String documentUrl) {
        String extension = getFileExtensionFromUrl(documentUrl);
        return String.format("app_%s_%s.%s", applicationId, documentType, extension);
    }

    public String getContentType(String fileName) {
        String ext = getFileExtensionFromUrl(fileName);
        switch (ext) {
            case "jpg":
            case "jpeg": return "image/jpeg";
            case "png": return "image/png";
            case "pdf": return "application/pdf";
            case "gif": return "image/gif";
            case "webp": return "image/webp";
            default: return "application/octet-stream";
        }
    }
}