package model.dto.BookStore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BSStatDTOs {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class RegularInventoryStatDTO {
        private long totalBooks;
        private long sellInventory;
        private long lendInventory;
        private long notSetInventory;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class DonationInventoryStatDTO {
        private long totalBooks;
        private long donated;
        private long donationStock;
        private long impactScore;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class SaleStatDTO {
        private long totalBooks;
        private long activeListings;
        private long inInventory;
        private long soldCount;

        private long avgSellPrice;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class LendStatDTO {
        private long totalBooks;
        private long activeListings;
        private long inInventory;
        private long onLoanCount;

        private long avgLendPrice;
    }
}
