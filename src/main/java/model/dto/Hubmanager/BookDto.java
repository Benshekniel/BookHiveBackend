package model.dto.Hubmanager;


import lombok.Data;
import java.time.LocalDateTime;

public class BookDto {
    @Data
    public class BookCreateDto {
        private String title;
        private String author;
        private String genre;
        private String condition;
        private String description;
        private String imageUrl;
        private String price;
        private String lendingTerms;
        private Long userId;
    }

    @Data
    public class BookResponseDto {
        private Long bookId;
        private String title;
        private String author;
        private String genre;
        private String condition;
        private String description;
        private String imageUrl;
        private String status;
        private LocalDateTime createdAt;
        private String price;
        private String lendingTerms;
        private String availability;
        private Long userId;
        private String userName;
    }
}
