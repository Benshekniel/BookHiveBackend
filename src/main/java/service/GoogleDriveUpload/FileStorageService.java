package service.GoogleDriveUpload;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


public interface FileStorageService {
        Map<String, String> uploadFile(MultipartFile file, String folderName, String randomFilename) throws IOException;
        byte[] downloadFile(String fileId) throws IOException;
        String getFileWebViewLink(String fileId) throws IOException;
        String getOrCreateFolder(String folderName) throws IOException;
        String generateRandomFilename(MultipartFile file) throws IOException;
}
