package controller;

import model.dto.AllUsersDTO;
//import model.dto.BooksDTO;
import model.dto.LoginDto;
import model.dto.UserBooksDTO;
import model.messageResponse.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.Login.LoginService;
import service.User.BooksService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:9999") // Allow Vite's port
@RequestMapping("/api")
public class UserController {

    @Autowired
    private BooksService booksService;


    @PostMapping("/saveBook-User")
    public ResponseEntity<?> saveBookasUser(@RequestBody UserBooksDTO userBooksDTO)
    {
        String response= booksService.addBook(userBooksDTO);
        if ("success".equals(response)) {
            return ResponseEntity.ok(Map.of("message", response));
        }
        return ResponseEntity.ok(Map.of("message", response));
    }

    @GetMapping("/getBooks")
    public ResponseEntity<List<UserBooksDTO>> getAllBooks() {
        List<UserBooksDTO> books = booksService.getAllBooks();
        return ResponseEntity.ok(books);
    }

}