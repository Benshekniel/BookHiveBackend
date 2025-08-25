package controller.BookStore;

import model.dto.BookStore.*;
import service.BookStore.BSBookService;
import service.BookStore.BookStoreService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bs-dashboard")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class DashboardController {

    private final BookStoreService bookStoreService;
    private final BSBookService bookService;

    // get some details for bookstore Performance Overview
    // get some details for bookstore Recent Orders
    // get some details for bookstore Inventory Summary

    @GetMapping("/getPerformance/{userId}")
    public ResponseEntity<BookStoreDTOs.ProfileBookStoreDTO> getPerformance (
            @PathVariable("userId") Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        return null;
    }

    @GetMapping("/getRecentOrders/{userId}")
    public ResponseEntity<BookStoreDTOs.ProfileBookStoreDTO> getRecentOrders (
            @PathVariable("userId") Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        return null;
    }

    @GetMapping("/getInventorySummary/{userId}")
    public ResponseEntity<BookStoreDTOs.ProfileBookStoreDTO> getInventorySummary (
            @PathVariable("userId") Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        return null;
    }
}
