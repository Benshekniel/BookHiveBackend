package model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "bookstore_books")
@Data
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ElementCollection
    @Column(columnDefinition = "text[]")
    private List<String> authors; // Maps to text[] column in bookstore_books

    @Column
    private String isbn;

    @Column
    private String publisher;

    @Column
    private Integer publicationYear;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private Double price;

    @Column(nullable = false)
    private Integer stockQuantity;

    // Constructor for essential fields
    public Book(String title, List<String> authors, String isbn, Integer stockQuantity) {
        this.title = title;
        this.authors = authors;
        this.isbn = isbn;
        this.stockQuantity = stockQuantity;
    }
}