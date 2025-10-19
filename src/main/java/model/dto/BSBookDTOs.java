//package model.dto;
//
//import model.entity.BSBook;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Map;
//
//public class BSBookDTOs {
//
//    @Data @NoArgsConstructor @AllArgsConstructor
//    public static class RegisterBookDTO {
//        private String title;
//        private List<String> authors;
//        private List<String> genres;
//        private String description;
//        private BSBook.BookCondition condition;
//
//        private List<String> imageUrls;
//        private List<String> tags;
//
//        private BSBook.BookStatus status;
//
//        private BSBook.ListingType listingType;
//        private Map<String, BigDecimal> pricing;
//
//        private String terms;
//        private Integer lendingPeriod;
//
//        private String isbn;
//        private String language;
//        private Map<String, String> seriesInfo;
//
//        private Integer bookCount;
//
//        private Integer ownerID;
//    }
//    @Data @NoArgsConstructor @AllArgsConstructor
//    public static class UpdateBookDTO {
//        private String title;
//        private List<String> authors;
//        private List<String> genres;
//        private String description;
//        private BSBook.BookCondition condition;
//
//        private List<String> imageUrls;
//        private List<String> tags;
//
//        private BSBook.BookStatus status;
//
//        private BSBook.ListingType listingType;
//        private Map<String, BigDecimal> pricing;
//
//        private String terms;
//        private Integer lendingPeriod;
//
//        private String isbn;
//        private String publisher;
//        private Integer publishedYear;
//        private String language;
//        private Integer pageCount;
//        private Map<String, String> seriesInfo;
//
//        private Integer bookCount;
//
//    }
//    @Data @NoArgsConstructor @AllArgsConstructor
//    public static class ViewBookDTO {
//        private String title;
//
//    }
//}
//
////private Integer bookId;
////private String title;
////private List<String> authors;
////private List<String> genres;
////private List<String> imageUrls;
////private List<String> tags;
////private BSBook.BookCondition condition;
////private String description;
////private BSBook.BookStatus status;
////private BSBook.BookAvailability availability;
////private BSBook.ListingType listingType;
////private Map<String, BigDecimal> pricing;
////private String terms;
////private String isbn;
////private String publisher;
////private Integer publishedYear;
////private String language;
////private Integer pageCount;
////private Integer lendingPeriod;
////private Integer bookCount;
////private Integer favouritesCount;
////private Map<String, String> seriesInfo;
////private LocalDateTime createdAt;
////private LocalDateTime updatedAt;
////private Integer ownerID;