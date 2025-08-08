package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import service.GoogleDriveUpload.FileStorageService;

import java.io.IOException;

@RestController
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/files/webViewLink/{fileId}")
    public ResponseEntity<String> getFileWebViewLink(@PathVariable String fileId) throws IOException {
        String webViewLink = fileStorageService.getFileWebViewLink(fileId);
        return ResponseEntity.ok(webViewLink);
    }
}
