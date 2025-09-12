package controller.BookStore;

import model.dto.BookStore.BSBookDTOs;
import model.dto.BookStore.BSBookDTOs.RegisterBookDTO;
import model.dto.BookStore.BSBookDTOs.BookDetailsDTO;
import service.BookStore.BSBookService;
import service.BookStore.BookStoreService;

import service.GoogleDriveUpload.FileStorageService;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RestController
@RequestMapping("/api/bs-book")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BSBookController {

    private final BSBookService bookService;
    private final FileStorageService fileStorageService;
    private final BookStoreService bookStoreService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerBook (
            @RequestPart("bookData") RegisterBookDTO regBookDTO,
            @RequestPart("coverImage") MultipartFile coverImage ) throws IOException {

        if (coverImage == null || coverImage.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cover image is required.");

        Integer userId = regBookDTO.getUserId();
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        String coverImageRandName = fileStorageService.generateRandomFilename(coverImage);
        fileStorageService.uploadFile(coverImage, "BSBook/coverImage", coverImageRandName);

        regBookDTO.setCoverImage(coverImageRandName);

        boolean saved = bookService.registerBook(regBookDTO, storeId);

        if (saved) return ResponseEntity.ok("Book(s) added successfully");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book(s) could not be added!");
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<String> updateBook (
            @PathVariable("bookId") Integer bookId,
            @RequestBody BookDetailsDTO bookDTO ) {

        boolean updated = bookService.updateBook(bookId, bookDTO);
        if (updated) return ResponseEntity.ok("Book updated successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book Not found!");
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook (
            @PathVariable("bookId") Integer bookId ) {

        boolean updated = bookService.deleteBook(bookId);
        if (updated) return ResponseEntity.ok("Book deleted successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book Not found!");
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<BSBookDTOs.BookDetailsDTO> getBook (@PathVariable("bookId") Integer bookId ) {

        BSBookDTOs.BookDetailsDTO formattedBook = bookService.getBookById(bookId);
        if (formattedBook == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(formattedBook);
    }

}
