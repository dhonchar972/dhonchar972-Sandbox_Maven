package com.sandbox_maven.package3.controllers;

import com.sandbox_maven.package3.entities.Book;
import com.sandbox_maven.package3.services.BookService;
import com.sandbox_maven.package3.util.CustomResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public CustomResponse<Book> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/books/{title}")
    public CustomResponse<Book> getBookByTitle(@PathVariable("title") String title) {
        return bookService.getBookByTitle(title);
    }

    @PostMapping("/books")
    public CustomResponse<Book> addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }
}
