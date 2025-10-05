package controller;

import model.dto.*;
//import model.dto.BooksDTO;
import model.messageResponse.LoginResponse;
import model.entity.Competitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.FileUpload.UploadService;
import service.GoogleDriveUpload.FileStorageService;
import service.Login.LoginService;
import service.User.BooksService;
import service.User.UserCompetitionService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:9999") // Allow Vite's port
@RequestMapping("/api")
public class UserController {

    @Autowired
    private BooksService booksService;


    @Autowired
    private UploadService uploadService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserCompetitionService userCompetitionService;

    //Books APIs

    @PostMapping("/saveBook-User")
    public ResponseEntity<?> saveBookasUser(
            @RequestPart("coverImage") MultipartFile bookImage,
            @RequestPart("bookData") UserBooksDTO userBooksDTO)
            throws IOException
    {

        String bookImageName = fileStorageService.generateRandomFilename(bookImage);
        // Assign random filenames to DTO
        userBooksDTO.setBookImage(bookImageName);

        String response= booksService.addBook(userBooksDTO);
        if ("success".equals(response)) {
            Map<String, String> uploadResult = fileStorageService.uploadFile(bookImage, "userBooks", bookImageName);
            return ResponseEntity.ok(Map.of("message", response));
        }
        return ResponseEntity.ok(Map.of("message", response));
    }

    @GetMapping("/getBooks")
    public ResponseEntity<List<UserBooksDTO>> getAllBooks() {
        List<UserBooksDTO> books = booksService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/getBook/{id}")
    public ResponseEntity<UserBooksDTO> getBookById(@PathVariable Long id) {
        UserBooksDTO book = booksService.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
            ("/updateBook/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody UserBooksDTO userBooksDTO) {
        String response = booksService.updateBook(id, userBooksDTO);
        return ResponseEntity.ok(Map.of("message", response));
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        String response = booksService.deleteBook(id);
        return ResponseEntity.ok(Map.of("message", response));
    }

    //Competition APIs

    @GetMapping("/getAllCompetitions")
    public ResponseEntity<List<Map<String, Object>>> getAllCompetitions() {
        List<Map<String, Object>> competitions = userCompetitionService.getAllUserCompetitionsMapped();
        return ResponseEntity.ok(competitions);
    }

    @PostMapping("/userSaveStory")
    public ResponseEntity<Map<String, String>>  userSaveStory(
            @RequestBody CompetitionSubmissionsDTO competitionSubmissionsDTO) {

        String response = userCompetitionService.saveSubmitStory(competitionSubmissionsDTO);
            return ResponseEntity.ok(Map.of("message", response));
    }

    // Add this to your Controller
    @GetMapping("/user/getDraftSubmission")
    public ResponseEntity<?> getDraftSubmission(
            @RequestParam String competitionId,
            @RequestParam String email) {

        CompetitionSubmissionsDTO draft = userCompetitionService.getDraftSubmission(competitionId, email);

        if (draft != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "success");
            response.put("submission", draft);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "not_found");
            return ResponseEntity.ok(response);
        }
    }



}