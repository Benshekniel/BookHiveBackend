package model.dto;

import model.entity.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BookDTOs {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class RegisterBookDTO {
        private String title;
        private List<String> authors;
        private List<String> genres;
        private String description;
        private Book.BookCondition condition;

        private List<String> imageUrls;
        private List<String> tags;

        private Book.BookStatus status;

        private Book.ListingType listingType;
        private Map<String, BigDecimal> pricing;

        private String terms;
        private Integer lendingPeriod;

        private String isbn;
        private String language;
        private Map<String, String> seriesInfo;

        private Integer bookCount;

        private Integer ownerID;
    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class UpdateBookDTO {
        private String title;
        private List<String> authors;
        private List<String> genres;
        private String description;
        private Book.BookCondition condition;

        private List<String> imageUrls;
        private List<String> tags;

        private Book.BookStatus status;

        private Book.ListingType listingType;
        private Map<String, BigDecimal> pricing;

        private String terms;
        private Integer lendingPeriod;

        private String isbn;
        private String publisher;
        private Integer publishedYear;
        private String language;
        private Integer pageCount;
        private Map<String, String> seriesInfo;

        private Integer bookCount;

    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ViewBookDTO {
        private String title;

    }
}

//private Integer bookId;
//private String title;
//private List<String> authors;
//private List<String> genres;
//private List<String> imageUrls;
//private List<String> tags;
//private Book.BookCondition condition;
//private String description;
//private Book.BookStatus status;
//private Book.BookAvailability availability;
//private Book.ListingType listingType;
//private Map<String, BigDecimal> pricing;
//private String terms;
//private String isbn;
//private String publisher;
//private Integer publishedYear;
//private String language;
//private Integer pageCount;
//private Integer lendingPeriod;
//private Integer bookCount;
//private Integer favouritesCount;
//private Map<String, String> seriesInfo;
//private LocalDateTime createdAt;
//private LocalDateTime updatedAt;
//private Integer ownerID;