package controller;

import model.dto.AllUsersDTO;
//import model.dto.BooksDTO;
import model.dto.LoginDto;
import model.dto.UserBooksDTO;
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
import model.dto.CompetitionDTO;

import java.io.IOException;
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


    @GetMapping("/getAllCompetitions")
    public ResponseEntity<List<Map<String, Object>>> getAllCompetitions() {
        List<Map<String, Object>> competitions = userCompetitionService.getAllUserCompetitionsMapped();
        return ResponseEntity.ok(competitions);
    }

}