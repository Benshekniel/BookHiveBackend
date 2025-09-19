package controller.BookStore;

import model.dto.BookStore.BSInventoryDTOs;
import service.BookStore.BSInventoryService;
import service.BookStore.BookStoreService;
import service.GoogleDriveUpload.FileStorageService;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/bs-inventory")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BSInventoryController {

    private final BSInventoryService inventoryService;
    private final FileStorageService fileStorageService;
    private final BookStoreService bookStoreService;

    @PostMapping(path= "/new", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerItem (
            @RequestPart("newItemData") BSInventoryDTOs.RegisterDTO registerDTO,
            @RequestPart("coverImage") MultipartFile coverImage ) throws IOException {

        if (coverImage == null || coverImage.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cover image is required.");

        String coverImageRandName = fileStorageService.generateRandomFilename(coverImage);
        fileStorageService.uploadFile(coverImage, "BSItem/coverImage", coverImageRandName);

        Integer userId = registerDTO.getUserId();
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        registerDTO.setCoverImage(coverImageRandName);

        boolean saved = inventoryService.createInventory(registerDTO, storeId);

        if (saved) return ResponseEntity.ok("Inventory created successfully");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inventory could not be created!");
    }

    @PutMapping(path="/edit", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> editItem (
            @RequestPart("editItemData") BSInventoryDTOs.EditDTO editDTO,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage ) throws IOException {

        if (coverImage != null) {
            String coverImageRandName = fileStorageService.generateRandomFilename(coverImage);
            fileStorageService.uploadFile(coverImage, "BSItem/coverImage", coverImageRandName);
            editDTO.setCoverImage(coverImageRandName);
        }

        boolean saved = inventoryService.editInventoryItem(editDTO);

        if (saved) return ResponseEntity.ok("Inventory created successfully");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inventory could not be created!");
    }



    @GetMapping("/inventoryOne/{inventoryId}")
    public ResponseEntity<BSInventoryDTOs.FullInventoryDTO> getInventoryItem (
            @PathVariable Integer inventoryId) {

        BSInventoryDTOs.FullInventoryDTO item = inventoryService.getInventoryItem(inventoryId);
        if (item == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(item);
    }

    @GetMapping("/regularList/{userId}")
    public ResponseEntity<List<BSInventoryDTOs.ConciseRegularDTO>> getRegularInventory (
            @PathVariable Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        List<BSInventoryDTOs.ConciseRegularDTO> books = inventoryService.getRegularInventory(storeId);
        if (books.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/donationList/{userId}")
    public ResponseEntity<List<BSInventoryDTOs.ConciseDonationDTO>> getDonationInventory (
            @PathVariable Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        List<BSInventoryDTOs.ConciseDonationDTO> books = inventoryService.getDonationInventory(storeId);
        if (books.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(books);
    }



    @GetMapping("/stockUpdate/{inventoryId}")
    public ResponseEntity<BSInventoryDTOs.UpdateCountDTO> getStockUpdateItem (
            @PathVariable Integer inventoryId ) {

        BSInventoryDTOs.UpdateCountDTO item = inventoryService.getStockUpdateItem(inventoryId);
        if (item == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(item);
    }

    @PutMapping("/stockUpdate")
    public ResponseEntity<String> updateStockItem (
            @RequestPart("newStockDetails") BSInventoryDTOs.UpdateCountDTO updateCountDTO ) {

        boolean updated = inventoryService.adjustStock(updateCountDTO);

        if (updated) return ResponseEntity.ok("Stock updated successfully");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update stock!");
    }



    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<String> deleteItem (
            @PathVariable("inventoryId") Integer inventoryId ) {

        boolean updated = inventoryService.deleteItem(inventoryId);
        if (updated) return ResponseEntity.ok("Item deleted successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item Not found!");
    }

}
