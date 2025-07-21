package model.dto;

import java.util.List;

public class BooksDTO {
    private String role;
    private String email;
    private String title;
    private List<String> authors;
    private List<String> genres;
    private List<String> imageUrls;
    private String condition;
    private String description;
    private String status;
    private String availability;
    private String listingType;
    private String pricing;
    private String isbn;
    private String publisher;
    private Integer publishedYear;
    private String language;
    private Integer pageCount;
    private Integer favouritesCount;
    private Integer lendingPeriod;
    private List<String> tags;
    private String seriesInfo;

    public BooksDTO(String role, String email, String title, List<String> authors, List<String> genres, List<String> imageUrls, String condition, String description, String status, String availability, String listingType, String pricing, String isbn, String publisher, Integer publishedYear, String language, Integer pageCount, Integer favouritesCount, Integer lendingPeriod, List<String> tags, String seriesInfo) {
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

    public BooksDTO() {
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

    @Override
    public String toString() {
        return "BooksDTO{" +
                "role='" + role + '\'' +
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
                '}';
    }
}
