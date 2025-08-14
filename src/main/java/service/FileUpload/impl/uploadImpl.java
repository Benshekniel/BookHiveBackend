package service.FileUpload.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import service.FileUpload.UploadService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class uploadImpl implements UploadService {

    @Override
    public void upload(MultipartFile file, String filename) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IOException("Filename must not be null or empty.");
        }

        // Create directory if it doesn't exist
        File dir = new File("F:\\zzz");
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new IOException("❌ Failed to create directory: " + dir.getAbsolutePath());
            }
        }

        // Save file with provided filename
        File destination = new File(dir, filename);
        file.transferTo(destination);

        System.out.println("✅ File saved successfully at: " + destination.getAbsolutePath());
    }

    @Override
    public String getFileType(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IOException("File type is not detectable.");
        }
        return contentType;
    }

    @Override
    public String getFileName(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IOException("File has no name.");
        }

        // Extract file extension
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }

        // Generate random filename with UUID + extension
        String randomFilename = UUID.randomUUID().toString() + extension;

        System.out.println("Generated random filename: " + randomFilename);

        return randomFilename;
    }
}
