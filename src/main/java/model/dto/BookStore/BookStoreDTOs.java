package model.dto.BookStore;

import model.entity.BookStore;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

public class BookStoreDTOs {

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RegisterBookStoreDTO {
        private String storeName;
        private String businessRegistrationNumber;
        private String description;
        private String email;
        private String phoneNumber;
        private String address;
        private String city;
        private String postalCode;
        private String district;
        private BookStore.BookType booksType;

        private Integer userId;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProfileBookStoreDTO {
        private String storeName;

        private String description;
        private String email;
        private String phoneNumber;
        private String address;
        private String city;
        private String postalCode;
        private String district;
        private BookStore.BookType booksType;

        private String storeLogoImage;
        private String storeImage;
        private Map<String, String> businessHours;
    }
}
