package springbootapi.booksapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springbootapi.booksapi.entity.Book;
import springbootapi.booksapi.services.BookService;


import java.util.List;

@RestController
public class BooksController {

    @Autowired
    private BookService bookService;

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/api/books")
    public List<Book> getBooks() {
        List<Book> book = bookService.retrieveBooks();
        return book;
    }

    @GetMapping("/api/books/{bookId}")
    public Book getBook(@PathVariable(name="bookId")Long bookId) {
        return bookService.getBook(bookId);
    }

    @PostMapping("/api/books")
    public void saveBook(Book book){
        bookService.saveBook(book);
        System.out.println("Book Saved Successfully");
    }

    @DeleteMapping("/api/books/{bookId}")
    public void deleteBook(@PathVariable(name="bookId")Long bookId){
        bookService.deleteBook(bookId);
        System.out.println("Book Deleted Successfully");
    }

    @PutMapping("/api/books/{bookId}")
    public void updateBook(@RequestBody Book book,
                               @PathVariable(name="bookId")Long bookId){
        Book b = bookService.getBook(bookId);
        if(b != null){
            bookService.updateBook(book);
        }

    }

}
