package springbootapi.booksapi.controllers;

import exceptions.errors.ConflictError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
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
    public ResponseEntity<?> getBook(@PathVariable(name="bookId")Long bookId) {
        ResponseEntity<Object> response = (ResponseEntity)bookService.getBook(bookId);
        return response;
    }

    @PostMapping("/api/books")
    public ResponseEntity<?> saveBook(@RequestBody Book book){
        if(book.getId()!=null){
            return new ResponseEntity<Object>("'id' is not allowed to send", HttpStatus.BAD_REQUEST);
        }
        try {
            bookService.saveBook(book);
        } catch (DataIntegrityViolationException e){
            ConflictError ce = new ConflictError(e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Object>(ce.getError(), HttpStatus.CONFLICT);
        }
        System.out.println("Book Saved Successfully");
        return new ResponseEntity<Book>(book, HttpStatus.CREATED);
    }

    @DeleteMapping("/api/books/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable(name="bookId")Long bookId){
        try {
            bookService.deleteBook(bookId);
        } catch (EmptyResultDataAccessException erdae){
            return new ResponseEntity<Object>("Book '" + bookId + "' does not exists", HttpStatus.NOT_FOUND);
        }
        System.out.println("Book Deleted Successfully");
        return new ResponseEntity<Object>("Book '" + bookId + "' deleted", HttpStatus.OK);
    }

    @PutMapping("/api/books/{bookId}")
    public ResponseEntity<?>  updateBook(@RequestBody Book book, @PathVariable(name="bookId")Long bookId){
        Book bookToUpdate = null;
        if(book.getId()!=null){
            return new ResponseEntity<Object>("'id' is not allowed to send", HttpStatus.BAD_REQUEST);
        }

        ResponseEntity bookResponse = bookService.getBook(bookId);
        if(bookResponse.getStatusCode()==HttpStatus.NOT_FOUND) {
            return bookResponse;
        }
        else {
            bookToUpdate = (Book)bookResponse.getBody();
            bookToUpdate.setAuthor(book.getAuthor());
            bookToUpdate.setTitle(book.getTitle());

            bookService.updateBook(bookToUpdate);
        }
        return new ResponseEntity<Book>(bookToUpdate, HttpStatus.OK);

    }

}
