package model.dto.BookStore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BSInventoryDTOs {

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RegisterDTO {
        private Integer userId;

        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> tags;

        private String description;
        private String terms;

        private String coverImage;

        private String isbn;
        private String publisher;
        private Integer publishedYear;
        private String language;
        private Integer pageCount;

        private String seriesName;
        private Integer seriesInstallment;
        private Integer seriesTotalBooks;

        private String condition;
        private Integer stockCount;

        private Boolean isForDonation;

        private Integer sellableCount;
        private BigDecimal sellPrice;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ToBSBookDTO {
        private Integer inventoryId;
        private List<String> images;

        private String terms;
        private String condition;

        private BigDecimal sellPrice;
        private BigDecimal lendFee;

        private Integer lendingPeriod;
        private BigDecimal lateFee;
        private BigDecimal minTrustScore;

        private Boolean isForSelling;
    }


    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EditDTO {
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

        private String seriesName;
        private Integer seriesInstallment;
        private Integer seriesTotalBooks;

        private String condition;

        private Boolean isForDonation;

        private Integer stockCount;
        private Integer sellableCount;
        private BigDecimal sellPrice;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdateCountDTO {
        private Integer inventoryId;

        private Integer stockCount;
        private Integer sellableCount;
    }


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

        private Boolean isForDonation;
        private Integer otherCount;

        private LocalDateTime createdAt;
    }


    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConciseRegularDTO {
        private Integer inventoryId;

        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> tags;

        private Integer seriesInstallment;
        private String seriesName;

        private String coverImage;

        private String isbn;

        private Integer favouritesCount;

        private String condition;

//        private Integer stockCount;
        private Integer sellableCount;
        private BigDecimal sellPrice;

        private LocalDateTime createdAt;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConciseDonationDTO {
        private Integer inventoryId;

        private String title;
        private List<String> authors;
        private List<String> genres;
        private List<String> tags;

        private Integer seriesInstallment;
        private String seriesName;

        private String coverImage;

        private String isbn;

        private String condition;

//        private Integer stockCount;
        private Integer otherCount;

        private LocalDateTime createdAt;
    }
}
