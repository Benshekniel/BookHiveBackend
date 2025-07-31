package controller;

import model.dto.BookStoreDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.BookStore.BookStoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookstore")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BookStoreController {

    private final BookStoreService bookStoreService;

//    @PostMapping
//    public ResponseEntity<String> registerBookStore (
//            @RequestBody BookStoreDTO.RegisterBookStoreDTO bookStoreDTO) {
//        return bookStoreService.registerBookStore(bookStoreDTO);
//    }

    @PostMapping
    public ResponseEntity<String> registerBookStore () {
        return bookStoreService.registerBookStore();
    }

}
