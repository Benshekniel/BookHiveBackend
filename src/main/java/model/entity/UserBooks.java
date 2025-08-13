package model.entity;

import Utils.JsonbListType;
import Utils.StringListConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_books")
public class UserBooks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id")
    private Long bookId;

    @Column(name = "user_email", length = 255)
    private String userEmail;

    @Column(name="title", length = 255)
    private String title;

    @Column(name="authors", columnDefinition = "jsonb")
    @Type(JsonbListType.class)
    private List<String> authors;

    @Column(name="genres", columnDefinition = "jsonb")
    @Type(JsonbListType.class)
    private List<String> genres;

    @Column(name="condition", length = 255)
    private String condition;

    @Column(name="for_sale")
    private Boolean forSale;

    @Column(name="price")
    private Double price;

    @Column(name="for_lend")
    private Boolean forLend;

    @Column(name="lending_amount")
    private Double lendingAmount;

    @Column(name="lending_period", length = 100)
    private String lendingPeriod;

    @Column(name="for_exchange")
    private Boolean forExchange;

    @Column(name="exchange_period", length = 100)
    private String exchangePeriod;

    @Column(name="description", length = 1000)
    private String description;

    @Column(name="location", length = 255)
    private String location;

    @Column(name="publish_year", length = 10)
    private String publishYear;

    @Column(name="isbn", length = 50)
    private String isbn;

    @Column(name="language", length = 100)
    private String language;

    @Column(name="hashtags", columnDefinition = "jsonb")
    @Type(JsonbListType.class)
    private List<String> hashtags;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="bookImage", length = 255)
    private String bookImage;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UserBooks(String userEmail, String title, List<String> authors, List<String> genres, String condition, Boolean forSale, Double price, Boolean forLend, Double lendingAmount, String lendingPeriod, Boolean forExchange, String exchangePeriod, String description, String location, String publishYear, String isbn, String language, List<String> hashtags) {
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

    public UserBooks() {
    }

    public UserBooks(String userEmail, String title, List<String> authors, List<String> genres, String condition, Boolean forSale, Double price, Boolean forLend, Double lendingAmount, String lendingPeriod, Boolean forExchange, String exchangePeriod, String description, String location, String publishYear, String isbn, String language, List<String> hashtags, String bookImage) {
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

    public Long getBookId() {
        return bookId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
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

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "UserBooks{" +
                "bookId=" + bookId +
                ", userEmail='" + userEmail + '\'' +
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
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
