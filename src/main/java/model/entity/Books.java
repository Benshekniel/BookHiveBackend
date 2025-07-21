package model.entity;

import Utils.StringListConverter;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "books")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_Id", length = 15)
    private Long bookId;

    @Column(name = "role")
    private String role;

    @Column(name = "email")
    private String email;

    @Column(name="title", length = 225)
    private String title;

    // Arrays stored as JSON
    @Column(name="authors",columnDefinition = "jsonb")
    @Convert(converter = StringListConverter.class)
    private List<String> authors;

    @Column(name="genres",columnDefinition = "jsonb")
    @Convert(converter = StringListConverter.class)
    private List<String> genres;

    @Column(name="imageUrls",columnDefinition = "jsonb")
    @Convert(converter = StringListConverter.class)
    private List<String> imageUrls;

    @Column(name="condition", length = 255)
    private String condition;

    @Column(name="description", length = 255)
    private String description;

    @Column(name="status", length = 255)
    private String status;

    @Column(name="availability", length = 255)
    private String availability;

    @Column(name="listingType", length = 255)
    private String listingType;

    // Pricing as JSON object
    @Column(name="pricing",columnDefinition = "jsonb")
    private String pricing;

    // Essential book info
    @Column(name="isbn", length = 255)
    private String isbn;
    @Column(name="publisher", length = 255)
    private String publisher;
    @Column(name="publishedYear", length = 255)
    private Integer publishedYear;
    @Column(name="language", length = 255)
    private String language;
    @Column(name="pageCount", length = 255)
    private Integer pageCount;

    @Column(name="favouritesCount", length = 255)
    private Integer favouritesCount;

    @Column(name="lendingPeriod", length = 255)
    private Integer lendingPeriod;

    @Column(name="tags",columnDefinition = "jsonb")
    @Convert(converter = StringListConverter.class)
    private List<String> tags; // ["bestseller", "classic", "award-winner"]

    // Series info as separate JSON object
    @Column(name="seriesInfo",columnDefinition = "jsonb")
    private String seriesInfo; // {"series": "Harry Potter", "seriesNumber": 1, "totalBooks": 7}

    // Timestamps
    @Column(name="createdAt")
    private LocalDateTime createdAt;
    @Column(name="updatedAt")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public Books(String role, String email, String title, List<String> authors, List<String> genres, List<String> imageUrls, String condition, String description, String status, String availability, String listingType, String pricing, String isbn, String publisher, Integer publishedYear, String language, Integer pageCount, Integer favouritesCount, Integer lendingPeriod, List<String> tags, String seriesInfo) {
        this.role = role;
        this.email = email;
        this.title = title;
        this.authors = authors;
        this.genres = genres;
        this.imageUrls = imageUrls;
        this.condition = condition;
        this.description = description;
        this.status = status;
        this.availability = availability;
        this.listingType = listingType;
        this.pricing = pricing;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
        this.language = language;
        this.pageCount = pageCount;
        this.favouritesCount = favouritesCount;
        this.lendingPeriod = lendingPeriod;
        this.tags = tags;
        this.seriesInfo = seriesInfo;
    }

    public Books() {
    }

    public Long getBookId() {
        return bookId;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public String getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getAvailability() {
        return availability;
    }

    public String getListingType() {
        return listingType;
    }

    public String getPricing() {
        return pricing;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public Integer getFavouritesCount() {
        return favouritesCount;
    }

    public Integer getLendingPeriod() {
        return lendingPeriod;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getSeriesInfo() {
        return seriesInfo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setListingType(String listingType) {
        this.listingType = listingType;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public void setFavouritesCount(Integer favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public void setLendingPeriod(Integer lendingPeriod) {
        this.lendingPeriod = lendingPeriod;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setSeriesInfo(String seriesInfo) {
        this.seriesInfo = seriesInfo;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", genres=" + genres +
                ", imageUrls=" + imageUrls +
                ", condition='" + condition + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", availability='" + availability + '\'' +
                ", listingType='" + listingType + '\'' +
                ", pricing='" + pricing + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishedYear=" + publishedYear +
                ", language='" + language + '\'' +
                ", pageCount=" + pageCount +
                ", favouritesCount=" + favouritesCount +
                ", lendingPeriod=" + lendingPeriod +
                ", tags=" + tags +
                ", seriesInfo='" + seriesInfo + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
