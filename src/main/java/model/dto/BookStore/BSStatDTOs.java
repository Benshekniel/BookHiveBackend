package model.dto.BookStore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BSStatDTOs {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class SaleStatDTO {
        private long totalBooks;
        private long activeListings;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class LendStatDTO {
        private long totalBooks;
        private long activeListings;
        private long avgPrice;
    }
}
