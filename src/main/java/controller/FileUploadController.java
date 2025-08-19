package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import service.GoogleDriveUpload.FileStorageService;

import java.io.IOException;
import java.util.Map;

@RestController
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload/idFront")
    public ResponseEntity<Map<String, String>> uploadIdFront(@RequestParam("file") MultipartFile file) throws IOException {
        String randomFilename = fileStorageService.generateRandomFilename(file);
        Map<String, String> result = fileStorageService.uploadFile(file, "idFront", randomFilename);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/upload/idBack")
    public ResponseEntity<Map<String, String>> uploadIdBack(@RequestParam("file") MultipartFile file) throws IOException {
        String randomFilename = fileStorageService.generateRandomFilename(file);
        Map<String, String> result = fileStorageService.uploadFile(file, "idBack", randomFilename);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/upload/billImage")
    public ResponseEntity<Map<String, String>> uploadBillImage(@RequestParam("file") MultipartFile file) throws IOException {
        String randomFilename = fileStorageService.generateRandomFilename(file);
        Map<String, String> result = fileStorageService.uploadFile(file, "billImage", randomFilename);
        return ResponseEntity.ok(result);
    }
}
