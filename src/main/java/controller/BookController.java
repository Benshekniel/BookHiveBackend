package controller;

import service.BookStore.BookService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookstore")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

}
