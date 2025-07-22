package model.dto.BookStore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import model.entity.Book;

import java.util.List;

public class BookDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookCreateDTO {
        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> imageUrls;
        private String condition; // BookCondition enum as string
        private String description;
        private String status; // BookStatus enum as string
        private String listingType; // ListingType enum as string
        private String pricing; // JSON string: {"sellingPrice": 25.99, "lendingPrice": 5.00, "depositAmount": 15.00}
        private String isbn;
        private String publisher;
        private Integer publishedYear;
        private String language;
        private Integer pageCount;
        private Integer lendingPeriod;
        private Integer bookCount;
        private List<String> tags;
        private String seriesInfo; // JSON string: {"series": "Harry Potter", "seriesNumber": 1, "totalBooks": 7}
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookUpdateDTO {
        private Integer bookId;
        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> imageUrls;
        private String condition;
        private String description;
        private String status;
        private String listingType;
        private String pricing;
        private String isbn;
        private String publisher;
        private Integer publishedYear;
        private String language;
        private Integer pageCount;
        private Integer lendingPeriod;
        private Integer bookCount;
        private List<String> tags;
        private String seriesInfo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookResponseDTO {
        private Integer bookId;
        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> imageUrls;
        private String condition;
        private String description;
        private String status;
        private String listingType;
        private String pricing;
        private String isbn;
        private String publisher;
        private Integer publishedYear;
        private String language;
        private Integer pageCount;
        private Integer lendingPeriod;
        private Integer bookCount;
        private Integer favouritesCount;
        private List<String> tags;
        private String seriesInfo;
        private String createdAt;
        private String updatedAt;
//        private Long userId;

        // Helper method to convert from Entity
        public static BookResponseDTO fromEntity(Book book) {
            BookResponseDTO dto = new BookResponseDTO();
            dto.setBookId(book.getBookId());
            dto.setTitle(book.getTitle());
            dto.setAuthors(book.getAuthors());
            dto.setGenres(book.getGenres());
            dto.setImageUrls(book.getImageUrls());
            dto.setCondition(book.getCondition().toString());
            dto.setDescription(book.getDescription());
            dto.setStatus(book.getStatus().toString());
            dto.setListingType(book.getListingType().toString());
            dto.setPricing(book.getPricing());
            dto.setIsbn(book.getIsbn());
            dto.setPublisher(book.getPublisher());
            dto.setPublishedYear(book.getPublishedYear());
            dto.setLanguage(book.getLanguage());
            dto.setPageCount(book.getPageCount());
            dto.setLendingPeriod(book.getLendingPeriod());
            dto.setBookCount(book.getBookCount());
            dto.setFavouritesCount(book.getFavouritesCount());
            dto.setTags(book.getTags());
            dto.setSeriesInfo(book.getSeriesInfo());
            dto.setCreatedAt(book.getCreatedAt().toString());
            dto.setUpdatedAt(book.getUpdatedAt().toString());
//            dto.setUserId(book.getUserId());
            return dto;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookListDTO {
        private Integer bookId;
        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> imageUrls;
        private String condition;     // e.g. "NEW", "USED_GOOD"
        private String status;        // e.g. "AVAILABLE", "SOLD"
        private String listingType;   // e.g. "SELL", "LEND"
        private String pricing;       // optional short form if needed
        private Integer favouritesCount;
    }

    // For bulk operations (bookstore specific)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookBulkCreateDTO {
        private List<BookCreateDTO> books;
    }
}