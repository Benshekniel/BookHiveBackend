package model.dto;

import java.util.List;

public class UserBooksDTO {

    private String userEmail;             // assuming String (or Integer if numeric)
    private String title;
    private List<String> authors;      // from author string split by commas
    private List<String> genres;       // genre array
    private String condition;
    private Boolean forSale;
    private Double price;
    private Boolean forLend;
    private Double lendingAmount;
    private String lendingPeriod;
    private Boolean forExchange;
    private String exchangePeriod;
    private String description;
    private String location;
    private String publishYear;        // can be String or Integer depending on your design
    private String isbn;
    private String language;
    private List<String> hashtags;     // from hashtags array
    private String bookImage;


    //Without book image
    public UserBooksDTO(String userEmail, String title, List<String> authors, List<String> genres, String condition, Boolean forSale, Double price, Boolean forLend, Double lendingAmount, String lendingPeriod, Boolean forExchange, String exchangePeriod, String description, String location, String publishYear, String isbn, String language, List<String> hashtags) {
        this.userEmail = userEmail;
        this.title = title;
        this.authors = authors;
        this.genres = genres;
        this.condition = condition;
        this.forSale = forSale;
        this.price = price;
        this.forLend = forLend;
        this.lendingAmount = lendingAmount;
        this.lendingPeriod = lendingPeriod;
        this.forExchange = forExchange;
        this.exchangePeriod = exchangePeriod;
        this.description = description;
        this.location = location;
        this.publishYear = publishYear;
        this.isbn = isbn;
        this.language = language;
        this.hashtags = hashtags;
    }

    //With book image
    public UserBooksDTO(String userEmail, String title, List<String> authors, List<String> genres, String condition, Boolean forSale, Double price, Boolean forLend, Double lendingAmount, String lendingPeriod, Boolean forExchange, String exchangePeriod, String description, String location, String publishYear, String isbn, String language, List<String> hashtags, String bookImage) {
        this.userEmail = userEmail;
        this.title = title;
        this.authors = authors;
        this.genres = genres;
        this.condition = condition;
        this.forSale = forSale;
        this.price = price;
        this.forLend = forLend;
        this.lendingAmount = lendingAmount;
        this.lendingPeriod = lendingPeriod;
        this.forExchange = forExchange;
        this.exchangePeriod = exchangePeriod;
        this.description = description;
        this.location = location;
        this.publishYear = publishYear;
        this.isbn = isbn;
        this.language = language;
        this.hashtags = hashtags;
        this.bookImage = bookImage;
    }

    public UserBooksDTO() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getCondition() {
        return condition;
    }

    public Boolean getForSale() {
        return forSale;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getForLend() {
        return forLend;
    }

    public Double getLendingAmount() {
        return lendingAmount;
    }

    public String getLendingPeriod() {
        return lendingPeriod;
    }

    public Boolean getForExchange() {
        return forExchange;
    }

    public String getExchangePeriod() {
        return exchangePeriod;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getPublishYear() {
        return publishYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getLanguage() {
        return language;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setForSale(Boolean forSale) {
        this.forSale = forSale;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setForLend(Boolean forLend) {
        this.forLend = forLend;
    }

    public void setLendingAmount(Double lendingAmount) {
        this.lendingAmount = lendingAmount;
    }

    public void setLendingPeriod(String lendingPeriod) {
        this.lendingPeriod = lendingPeriod;
    }

    public void setForExchange(Boolean forExchange) {
        this.forExchange = forExchange;
    }

    public void setExchangePeriod(String exchangePeriod) {
        this.exchangePeriod = exchangePeriod;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    @Override
    public String toString() {
        return "UserBooksDTO{" +
                "userEmail='" + userEmail + '\'' +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", genres=" + genres +
                ", condition='" + condition + '\'' +
                ", forSale=" + forSale +
                ", price=" + price +
                ", forLend=" + forLend +
                ", lendingAmount=" + lendingAmount +
                ", lendingPeriod='" + lendingPeriod + '\'' +
                ", forExchange=" + forExchange +
                ", exchangePeriod='" + exchangePeriod + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", publishYear='" + publishYear + '\'' +
                ", isbn='" + isbn + '\'' +
                ", language='" + language + '\'' +
                ", hashtags=" + hashtags +
                '}';
    }
}
