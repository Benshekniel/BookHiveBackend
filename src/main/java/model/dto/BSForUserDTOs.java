package model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BSForUserDTOs {

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FullInventoryDTO {
        private Integer inventoryId;

        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> tags;

        private String coverImage;

        private String description;
        private String terms;

        private String isbn;
        private String publisher;
        private Integer publishedYear;
        private String language;
        private Integer pageCount;

        private Integer favouritesCount;

        private String seriesName;
        private Integer seriesInstallment;
        private Integer seriesTotalBooks;

        private String condition;

        private Integer stockCount;
        private Integer sellableCount;
        private BigDecimal sellPrice;

        private LocalDateTime createdAt;
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

}
