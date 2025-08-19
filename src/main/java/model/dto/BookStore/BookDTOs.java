package model.dto.BookStore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BookDTOs {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class RegisterBookDTO {
        private String title;

    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class UpdateBookDTO {
        private String title;

    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ViewBookDTO {
        private String title;

    }
}
