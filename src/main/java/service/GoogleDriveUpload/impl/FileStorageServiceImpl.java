package service.GoogleDriveUpload.impl;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import service.GoogleDriveUpload.FileStorageService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private Drive driveService;

    @Override
    public Map<String, String> uploadFile(MultipartFile file, String folderName, String randomFilename) throws IOException {
        // Validate input
        if (file == null || file.isEmpty()) {
            throw new IOException("File must not be null or empty.");
        }
        if (randomFilename == null || randomFilename.trim().isEmpty()) {
            throw new IOException("Random filename must not be null or empty.");
        }

        // Get or create folder
        String folderId = getOrCreateFolder(folderName);

        // Set file metadata with provided random filename
        File fileMetadata = new File();
        fileMetadata.setName(randomFilename);
        fileMetadata.setParents(Collections.singletonList(folderId));
        fileMetadata.setMimeType(file.getContentType());

        // Upload file to Google Drive
        try (InputStream inputStream = file.getInputStream()) {
            File uploadedFile = driveService.files()
                    .create(fileMetadata, new InputStreamContent(file.getContentType(), inputStream))
                    .setFields("id")
                    .execute();
            Map<String, String> result = new HashMap<>();
            result.put("fileId", uploadedFile.getId());
            result.put("fileName", randomFilename);
            return result;
        }
    }

    @Override
    public byte[] downloadFile(String fileId) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public String getFileWebViewLink(String fileId) throws IOException {
        return driveService.files()
                .get(fileId)
                .setFields("webViewLink")
                .execute()
                .getWebViewLink();
    }

    @Override
    public String getOrCreateFolder(String folderName) throws IOException {
        String query = "name = '" + folderName + "' and mimeType = 'application/vnd.google-apps.folder' and trashed = false";
        FileList fileList = driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();
        if (fileList.getFiles() != null && !fileList.getFiles().isEmpty()) {
            return fileList.getFiles().get(0).getId();
        }
        File folderMetadata = new File();
        folderMetadata.setName(folderName);
        folderMetadata.setMimeType("application/vnd.google-apps.folder");
        File folder = driveService.files().create(folderMetadata)
                .setFields("id")
                .execute();
        return folder.getId();
    }

    @Override
    public String generateRandomFilename(MultipartFile file) throws IOException {
        // Validate input
        if (file == null || file.isEmpty()) {
            throw new IOException("File must not be null or empty.");
        }

        // Get original filename and extract extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IOException("File has no name.");
        }
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }

        // Generate random filename with UUID and original extension
        return UUID.randomUUID().toString() + extension;
    }

    @Override
    public Drive getDriveService() {
        return driveService;
    }

    @Override
    public String getFileWebViewLinkByName(String fileName, String folderName) throws IOException {
        String folderId = getOrCreateFolder(folderName);
        String query = String.format("name = '%s' and '%s' in parents and trashed = false", fileName, folderId);
        FileList fileList = driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(webViewLink)")
                .execute();
        if (fileList.getFiles() != null && !fileList.getFiles().isEmpty()) {
            return fileList.getFiles().get(0).getWebViewLink();
        }
        throw new IOException("File not found: " + fileName);
    }

    @Override
    public String getFileAsBase64(String fileName, String folderName) throws IOException {
        String folderId = getOrCreateFolder(folderName);
        String query = String.format("name = '%s' and '%s' in parents and trashed = false", fileName, folderId);
        FileList fileList = driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, mimeType)")
                .execute();
        if (fileList.getFiles() != null && !fileList.getFiles().isEmpty()) {
            String fileId = fileList.getFiles().get(0).getId();
            String mimeType = fileList.getFiles().get(0).getMimeType();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
            byte[] fileBytes = outputStream.toByteArray();
            String base64String = Base64.getEncoder().encodeToString(fileBytes);
            return String.format("data:%s;base64,%s", mimeType, base64String);
        }
        throw new IOException("File not found: " + fileName);
    }

    @Override
    public String storeFile(MultipartFile imageFile, String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'storeFile'");
    }

    @Override
    public String getFileUrl(String fileName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFileUrl'");
    }

}
//import com.google.api.client.http.InputStreamContent;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.model.File;
//import com.google.api.services.drive.model.FileList;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import service.GoogleDriveUpload.FileStorageService;
//
//import com.google.api.client.http.InputStreamContent;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.model.File;
//import com.google.api.services.drive.model.FileList;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import service.GoogleDriveUpload.FileStorageService;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@Service
//public class FileStorageServiceImpl implements FileStorageService {
//
//    @Autowired
//    private Drive driveService;
//
//    @Override
//    public Map<String, String> uploadFile(MultipartFile file, String folderName) throws IOException {
//        // Validate input
//        if (file == null || file.isEmpty()) {
//            throw new IOException("File must not be null or empty.");
//        }
//
//        // Generate random filename with UUID and original extension
//        String originalFilename = file.getOriginalFilename();
//        if (originalFilename == null || originalFilename.trim().isEmpty()) {
//            throw new IOException("File has no name.");
//        }
//        String extension = "";
//        int dotIndex = originalFilename.lastIndexOf('.');
//        if (dotIndex >= 0) {
//            extension = originalFilename.substring(dotIndex);
//        }
//        String randomFilename = UUID.randomUUID().toString() + extension;
//
//        // Get or create folder
//        String folderId = getOrCreateFolder(folderName);
//
//        // Set file metadata with random filename
//        File fileMetadata = new File();
//        fileMetadata.setName(randomFilename);
//        fileMetadata.setParents(Collections.singletonList(folderId));
//        fileMetadata.setMimeType(file.getContentType());
//
//        // Upload file to Google Drive
//        try (InputStream inputStream = file.getInputStream()) {
//            File uploadedFile = driveService.files()
//                    .create(fileMetadata, new InputStreamContent(file.getContentType(), inputStream))
//                    .setFields("id")
//                    .execute();
//            Map<String, String> result = new HashMap<>();
//            result.put("fileId", uploadedFile.getId());
//            result.put("fileName", randomFilename);
//            return result;
//        }
//    }
//
//    @Override
//    public byte[] downloadFile(String fileId) throws IOException {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
//        return outputStream.toByteArray();
//    }
//
//    @Override
//    public String getFileWebViewLink(String fileId) throws IOException {
//        return driveService.files()
//                .get(fileId)
//                .setFields("webViewLink")
//                .execute()
//                .getWebViewLink();
//    }
//
//    @Override
//    public String getOrCreateFolder(String folderName) throws IOException {
//        String query = "name = '" + folderName + "' and mimeType = 'application/vnd.google-apps.folder' and trashed = false";
//        FileList fileList = driveService.files().list()
//                .setQ(query)
//                .setSpaces("drive")
//                .setFields("files(id, name)")
//                .execute();
//        if (fileList.getFiles() != null && !fileList.getFiles().isEmpty()) {
//            return fileList.getFiles().get(0).getId();
//        }
//        File folderMetadata = new File();
//        folderMetadata.setName(folderName);
//        folderMetadata.setMimeType("application/vnd.google-apps.folder");
//        File folder = driveService.files().create(folderMetadata)
//                .setFields("id")
//                .execute();
//        return folder.getId();
//    }
//}
