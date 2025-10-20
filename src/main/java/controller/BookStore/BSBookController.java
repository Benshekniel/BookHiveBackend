package controller.BookStore;

import model.dto.BookStore.BSBookDTOs;
import model.dto.BookStore.BSInventoryDTOs;
import model.dto.BookStore.BSStatDTOs;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bs-book")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BSBookController {

    private final BSBookService bookService;
    private final FileStorageService fileStorageService;
    private final BookStoreService bookStoreService;

    @PostMapping(path= "/fromInventory", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerItemFromInventory (
            @RequestPart("bookFromInventoryData") BSInventoryDTOs.ToBSBookDTO registerDTO,
            @RequestPart("images") List<MultipartFile> images ) throws IOException {

        if (images == null || images.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("At least 1 image is required.");

        List<String> savedImageNames = new ArrayList<>();
        for (MultipartFile image : images) {
            if (image.isEmpty()) continue;
            String randName = fileStorageService.generateRandomFilename(image);
            fileStorageService.uploadFile(image, "BSItem/images", randName);
            savedImageNames.add(randName);
        }
        registerDTO.setImages(savedImageNames);

        boolean saved = bookService.createBookFromInventory(registerDTO);

        if (saved) return ResponseEntity.ok("Listing created successfully");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Listing could not be created!");
    }

    @PostMapping(path= "/new", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerBook (
            @RequestPart("newBookData") BSBookDTOs.RegisterDTO newBookData,
            @RequestPart("coverImage") MultipartFile coverImage,
            @RequestPart("images") List<MultipartFile> images) throws IOException {

        if (coverImage == null || coverImage.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cover image is required.");

        String coverImageRandName = fileStorageService.generateRandomFilename(coverImage);
        fileStorageService.uploadFile(coverImage, "BSItem/coverImage", coverImageRandName);

        List<String> savedImageNames = new ArrayList<>();
        for (MultipartFile image : images) {
            if (image.isEmpty()) continue;
            String randName = fileStorageService.generateRandomFilename(image);
            fileStorageService.uploadFile(image, "BSItem/images", randName);
            savedImageNames.add(randName);
        }
        newBookData.setImages(savedImageNames);

        Integer userId = newBookData.getUserId();
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        newBookData.setCoverImage(coverImageRandName);

        boolean saved = bookService.createBook(newBookData, storeId);

        if (saved) return ResponseEntity.ok("Book created successfully");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book could not be created!");
    }


    @PutMapping(path="/edit", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> editItem (
            @RequestPart("editBookData") BSBookDTOs.EditDTO editDTO,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
            @RequestPart(value = "images", required = false) List<MultipartFile> images ) throws IOException {

        if (coverImage != null) {
            String coverImageRandName = fileStorageService.generateRandomFilename(coverImage);
            fileStorageService.uploadFile(coverImage, "BSItem/coverImage", coverImageRandName);
            editDTO.setCoverImage(coverImageRandName);
        }

        if (images != null) {
            List<String> savedImageNames = new ArrayList<>();
            for (MultipartFile image : images) {
                if (image.isEmpty()) continue;
                String randName = fileStorageService.generateRandomFilename(image);
                fileStorageService.uploadFile(image, "BSItem/images", randName);
                savedImageNames.add(randName);
            }
            editDTO.setImages(savedImageNames);
        }

        boolean saved = bookService.editBook(editDTO);

        if (saved) return ResponseEntity.ok("Book edited successfully");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book could not be edited!");
    }


    @GetMapping("/bookOne/{bookId}")
    public ResponseEntity<BSBookDTOs.FullBookDTO> getBookItem (
            @PathVariable Integer bookId) {

        BSBookDTOs.FullBookDTO item = bookService.getBookItem(bookId);
        if (item == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(item);
    }

    @GetMapping("/lendOnlyList/{userId}")
    public ResponseEntity<List<BSBookDTOs.ConciseLendOnlyDTO>> getLendOnlyList (
            @PathVariable Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        List<BSBookDTOs.ConciseLendOnlyDTO> books = bookService.getLendOnlyList(storeId);
        if (books.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/sellAlsoList/{userId}")
    public ResponseEntity<List<BSBookDTOs.ConciseSellAlsoDTO>> getSellAlsoList (
            @PathVariable Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        List<BSBookDTOs.ConciseSellAlsoDTO> books = bookService.getSellAlsoList(storeId);
        if (books.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(books);
    }



    @GetMapping("/stats/lendOnly/{userId}")
    public ResponseEntity<BSStatDTOs.LendOnlyStatDTO> getLendOnlyStats (
            @PathVariable Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        BSStatDTOs.LendOnlyStatDTO stats = bookService.getLendOnlyStats(storeId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/sellAlso/{userId}")
    public ResponseEntity<BSStatDTOs.SellAlsoStatDTO> getSellAlsoStats (
            @PathVariable Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        BSStatDTOs.SellAlsoStatDTO stats = bookService.getSellAlsoStats(storeId);
        return ResponseEntity.ok(stats);
    }



    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook (
            @PathVariable("bookId") Integer bookId ) {

        boolean updated = bookService.deleteBook(bookId);
        if (updated) return ResponseEntity.ok("Book deleted successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book Not found!");
    }
}
