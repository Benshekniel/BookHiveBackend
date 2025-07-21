package model.dto.BookStore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class BookStoreDTO {

    // For creating new bookstore
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookStoreCreateDTO {
        @NotBlank(message = "Store name is required")
        private String storeName;

        private String storeImageURL;
        private String description;
        private String businessHours;

        @NotBlank(message = "Books type is required")
        private String booksType;
        // No storeId, user info (auto-generated/from authenticated user)
    }

    // For updating bookstore
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookStoreUpdateDTO {
        private Integer storeId;
        private String storeName;
        private String storeImageURL;
        private String description;
        private String businessHours;
        private String booksType;
    }

    // For full bookstore details response
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookStoreResponseDTO {
        private Integer storeId;
        private String storeName;
        private String storeImageURL;
        private String description;
        private String businessHours;
        private String booksType;

        // User info
        private Integer userId;
        private String ownerName;
        private String email;
        private String phoneNumber;
        private String address;
        private LocalDateTime registeredDate; // from user.createdAt
    }

    // For bookstore listing (minimal info)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookStoreListDTO {
        private Integer storeId;
        private String storeName;
        private String storeImageURL;
        private String description;
        private String booksType;

        // Basic user info
        private String ownerName;
        private String address;
        private LocalDateTime registeredDate; // from user.createdAt
    }

    // For bookstore profile (public view)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookStorePublicDTO {
        private Integer storeId;
        private String storeName;
        private String storeImageURL;
        private String description;
        private String businessHours;
        private String booksType;

        // Public user info (no email)
        private String ownerName;
        private String phoneNumber;
        private String address;
        private LocalDateTime registeredDate; // from user.createdAt
    }
}