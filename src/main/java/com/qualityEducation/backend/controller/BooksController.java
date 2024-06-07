package com.qualityEducation.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qualityEducation.backend.model.Books;
import com.qualityEducation.backend.service.BooksService;

@RestController
public class BooksController {

    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @PostMapping(value = "/createBook")
    public ResponseEntity<String> uploadBook(@RequestParam("file") MultipartFile file,
            @ModelAttribute("book") Books book) {
        if (file.isEmpty()) {
            return ResponseEntity.ok("Image file is empty");
        }
        boolean isBookCreated = booksService.saveBook(book, file);
        if (isBookCreated) {
            return ResponseEntity.ok("Book created successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to create book");
        }
    }

    @GetMapping("/getBooks")
    public ResponseEntity<List<Books>> getBooks() {
        try {
            List<Books> books = booksService.getAllBooks();
            if (!books.isEmpty()) {
                return ResponseEntity.ok(books);
            }
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/getBookById/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable Long id) {
        try {
            Books book = booksService.BookById(id);
            if (book == null) {
                return ResponseEntity.status(204).build();
            }
            return ResponseEntity.ok(book);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
