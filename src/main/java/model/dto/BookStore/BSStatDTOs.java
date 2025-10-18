package model.dto.BookStore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class BSStatDTOs {

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DashboardStatsDTO {
        private long totalInventoryItems;
        private long totalBooksItems;
        private long totalTransactions;
        private BigDecimal totalTransactionsValue;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RegularInventoryDTO {
        private long totalBooks;            // SUM of stockCount
        private long totalSellable;         // SUM of sellableCount
        private long newBooks;              // count with condition = NEW

        private long lowStockAlerts;         // count where stockCount < threshold
        private List<String> lowStockTitles; // top 3 low stock records' titles

        private List<String> topTitles;     // top 3 most in stock titles
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DonationInventoryDTO {
        private long totalDonationBooks;

        private long donatedStock;
        private long pendingStock;

        private long donatedClients;
        private BigDecimal impactScore;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LendOnlyStatDTO {
        private long totalBooks;
        private long currentLent;

        private BigDecimal avgLendFee;
        private long lendPeriod;

        private List<String> mostCirculated;
        private List<String> mostFavourite;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SellAlsoStatDTO {
        private long totalBooks;

        private long soldCount;
        private BigDecimal sumSold;

        private BigDecimal avgSellPrice;
        private List<String> topGenres;
    }
}
