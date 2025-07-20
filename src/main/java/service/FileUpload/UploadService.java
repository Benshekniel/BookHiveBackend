package service.FileUpload;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface UploadService {
    // Upload file with specified filename
    void upload(MultipartFile file, String filename) throws IOException;

    // Get content type of file
    String getFileType(MultipartFile file) throws IOException;

    // Generate and return a random filename for the uploaded file
    String getFileName(MultipartFile file) throws IOException;
}
