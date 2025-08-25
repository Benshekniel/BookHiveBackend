package model.dto.BookStore;

import model.entity.BSBook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class BSBookDTOs {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class RegisterBookDTO {
        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> tags;

        private String description;
        private BSBook.BookCondition condition;

        private String coverImage;

        private BSBook.BookStatus status;

        private BSBook.ListingType listingType;
        private Map<String, BigDecimal> pricing;

        private String terms;
        private Integer lendingPeriod;

        private String isbn;
        private String language;
        private Map<String, String> seriesInfo;

        private Integer bookCount;
    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class UpdateBookDTO {
        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> tags;

        private String description;
        private BSBook.BookCondition condition;

        private String coverImage;
        private List<String> images;

        private BSBook.BookStatus status;

        private BSBook.ListingType listingType;
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
        private List<String> authors;
        private List<String> genres;
        private List<String> tags;

        private String description;
        private BSBook.BookCondition condition;

        private String coverImage;
        private List<String> images;

        private Map<String, String> seriesInfo;

        private Integer bookCount;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class BookListingDTO {
        private Integer bookId;

        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> tags;

        private BSBook.BookCondition condition;
        private BSBook.BookStatus status;
        private BSBook.ListingType listingType;

        private Map<String, BigDecimal> pricing;

        private String coverImage;
        private LocalDateTime createdAt;

        private Integer bookCount;
    }
}
