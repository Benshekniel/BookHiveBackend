package controller;

import com.google.api.services.drive.model.FileList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.GoogleDriveUpload.FileStorageService;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:9999") // Allow Vite's port
@RestController
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

//    @GetMapping("/files/webViewLink/{fileId}")
//    public ResponseEntity<String> getFileWebViewLink(@PathVariable String fileId) throws IOException {
//        String webViewLink = fileStorageService.getFileWebViewLink(fileId);
//        return ResponseEntity.ok(webViewLink);
//    }

//    @GetMapping("/getFileWebViewLinkByName")
//    public ResponseEntity<String> getFileWebViewLinkByName(
//            @RequestParam("fileName") String fileName,
//            @RequestParam("folderName") String folderName) {
//        try {
//            String webViewLink = fileStorageService.getFileWebViewLinkByName(fileName, folderName);
//            return ResponseEntity.ok(webViewLink);
//        } catch (IOException e) {
//            return ResponseEntity.status(404).body("File not found: " + fileName);
//        }
//    }

    @GetMapping("/getFileWebViewLinkByName")
    public ResponseEntity<String> getFileWebViewLinkByName(
            @RequestParam("fileName") String fileName,
            @RequestParam("folderName") String folderName) {
        try {
            String webViewLink = fileStorageService.getFileWebViewLinkByName(fileName, folderName);
            System.out.println("Returning webViewLink for fileName: " + fileName + ", folderName: " + folderName + ": " + webViewLink);
            return ResponseEntity.ok(webViewLink);
        } catch (IOException e) {
            System.err.println("Error fetching webViewLink for fileName: " + fileName + ", folderName: " + folderName + ": " + e.getMessage());
            return ResponseEntity.status(404).body("File not found: " + fileName);
        }
    }

    @GetMapping("/getFileAsBase64")
    public ResponseEntity<String> getFileAsBase64(
            @RequestParam("fileName") String fileName,
            @RequestParam("folderName") String folderName) {
        try {
            String base64Data = fileStorageService.getFileAsBase64(fileName, folderName);
            System.out.println("Returning base64 for fileName: " + fileName + ", folderName: " + folderName);
            return ResponseEntity.ok(base64Data);
        } catch (IOException e) {
            System.err.println("Error fetching base64 for fileName: " + fileName + ", folderName: " + folderName + ": " + e.getMessage());
            return ResponseEntity.status(404).body("File not found: " + fileName);
        }
    }

}
