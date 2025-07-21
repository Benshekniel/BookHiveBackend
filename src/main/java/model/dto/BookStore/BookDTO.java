package model.dto.BookStore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BookDTO {

    // For creating new books
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookCreateDTO {
        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> imageUrls;
        private String condition;
        private String description;
        private String status;
        private String availability;
        private String listingType;
        private String pricing;
        private String lendingTerms;
        private String isbn;
        private String publisher;
        private Integer publishedYear;
        private String language;
        private Integer pageCount;
        private List<String> tags;
        private String seriesInfo;
        // No bookId, createdAt, updatedAt, userId (auto-generated/set by system)
    }

    // For updating existing books
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookUpdateDTO {
        private Long bookId;  // Required for updates
        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> imageUrls;
        private String condition;
        private String description;
        private String status;
        private String availability;
        private String listingType;
        private String pricing;
        private String lendingTerms;
        private String isbn;
        private String publisher;
        private Integer publishedYear;
        private String language;
        private Integer pageCount;
        private List<String> tags;
        private String seriesInfo;
    }

    // For full book details response
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookResponseDTO {
        private Long bookId;
        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> imageUrls;
        private String condition;
        private String description;
        private String status;
        private String availability;
        private String listingType;
        private String pricing;
        private String lendingTerms;
        private String isbn;
        private String publisher;
        private Integer publishedYear;
        private String language;
        private Integer pageCount;
        private List<String> tags;
        private String seriesInfo;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long userId;
    }

    // For book listing pages (minimal info)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookListDTO {
        private Long bookId;
        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> imageUrls;
        private String condition;
        private String status;
        private String availability;
        private String listingType;
        private String pricing;
        private String isbn;
        private LocalDateTime createdAt;
        private Long userId;
    }

    // For bulk operations (bookstore specific)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookBulkCreateDTO {
        private List<BookCreateDTO> books;
    }
}