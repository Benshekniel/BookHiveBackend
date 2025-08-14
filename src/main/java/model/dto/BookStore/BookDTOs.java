package model.dto.BookStore;

import model.entity.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

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
