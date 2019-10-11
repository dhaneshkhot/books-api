package springbootapi.booksapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springbootapi.booksapi.entity.Book;
import springbootapi.booksapi.services.BookService;

import java.util.List;

@RestController
public class BooksController {

    @Autowired
    private BookService bookService;

    @GetMapping("/api/books")
    public List<Book> getBooks() {
        return bookService.retrieveBooks();
    }

    @GetMapping("/api/books/{bookId}")
    public ResponseEntity<?> getBook(@PathVariable(name="bookId")Long bookId) {
        return bookService.getBook(bookId);
    }

    @PostMapping("/api/books")
    public ResponseEntity<?> saveBook(@RequestBody Book book){
        return bookService.saveBook(book);
    }

    @DeleteMapping("/api/books/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable(name="bookId")Long bookId){
        return bookService.deleteBook(bookId);
    }

    @PutMapping("/api/books")
    public ResponseEntity<?>  updateBook(@RequestBody Book book){
        return  bookService.saveBook(book);
    }

}
