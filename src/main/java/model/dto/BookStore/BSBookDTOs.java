package model.dto.BookStore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BSBookDTOs {

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RegisterDTO {
        private Integer userId;

        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> tags;

        private String coverImage;
        private List<String> images;

        private String description;
        private String condition;

        private BigDecimal lendFee;
        private String terms;
        private Integer lendingPeriod;
        private BigDecimal lateFee;
        private BigDecimal minTrustScore;

        private String isbn;
        private String publisher;
        private Integer publishedYear;
        private String language;
        private Integer pageCount;

        private String seriesName;
        private Integer seriesInstallment;
        private Integer seriesTotalBooks;

        private Boolean isForSelling;
        private BigDecimal sellPrice;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EditDTO {
        private Integer bookId;

        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> tags;

        private String coverImage;
        private List<String> images;

        private String description;
        private String condition;

        private BigDecimal lendFee;
        private String terms;
        private Integer lendingPeriod;
        private BigDecimal lateFee;
        private BigDecimal minTrustScore;

        private Boolean isForSelling;
        private BigDecimal sellPrice;

        private String isbn;
        private String publisher;
        private Integer publishedYear;
        private String language;
        private Integer pageCount;

        private String seriesName;
        private Integer seriesInstallment;
        private Integer seriesTotalBooks;
    }


    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FullBookDTO {
        private Integer bookId;

        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> tags;

        private String coverImage;
        private List<String> images;

        private String description;
        private String condition;

        private BigDecimal lendFee;
        private String terms;
        private Integer lendingPeriod;
        private BigDecimal lateFee;
        private BigDecimal minTrustScore;

        private Boolean isForSelling;
        private BigDecimal sellPrice;

        private String isbn;
        private String publisher;
        private Integer publishedYear;
        private String language;
        private Integer pageCount;

        private Integer favouritesCount;

        private String seriesName;
        private Integer seriesInstallment;
        private Integer seriesTotalBooks;

        private LocalDateTime createdAt;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConciseLendOnlyDTO {
        private Integer bookId;

        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> tags;

        private Integer seriesInstallment;
        private String seriesName;

        private String coverImage;

        private String condition;
        private String status;

        private BigDecimal lendFee;
        private Integer lendingPeriod;
        private BigDecimal lateFee;
        private BigDecimal minTrustScore;

        private String isbn;

        private Integer circulations;
        private Integer favouritesCount;

        private LocalDateTime createdAt;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConciseSellAlsoDTO {
        private Integer bookId;

        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> tags;

        private Integer seriesInstallment;
        private String seriesName;

        private String coverImage;

        private String condition;
        private String status;

        private BigDecimal sellPrice;
        private BigDecimal lendFee;

        private Integer lendingPeriod;
        private BigDecimal lateFee;
        private BigDecimal minTrustScore;

        private String isbn;

        private Integer circulations;
        private Integer favouritesCount;

        private LocalDateTime createdAt;
    }

}
