package controller;
//
//import model.dto.BSBookDTOs.RegisterBookDTO;
//import model.dto.BSBookDTOs.UpdateBookDTO;
//import model.dto.BSBookDTOs.ViewBookDTO;
//import service.BookStore.BSBookService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bsbook")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BSBookController {

//    private final BSBookService bookService;
//
//    @PostMapping
//    public ResponseEntity<String> registerBook (
//            @RequestBody RegisterBookDTO bookDTO) {
//        // ownerId will need to be taken from the logged in user's userId
//        Integer ownerID = 603;
//        return bookService.registerBook(bookDTO, ownerID);
//    }
//
//    @GetMapping("/{bookId}")
//    public ResponseEntity<ViewBookDTO> getBook (
//            @PathVariable("bookId") Integer bookId) {
//        return bookService.getBookById(bookId);
//    }
//
//    @PutMapping("/{bookId}")
//    public ResponseEntity<String> updateBook (
//            @PathVariable("bookId") Integer bookId,
//            @RequestBody UpdateBookDTO bookDTO) {
//
//        return bookService.updateBook(bookId, bookDTO);
//    }
}
