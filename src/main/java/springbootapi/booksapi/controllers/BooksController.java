package springbootapi.booksapi.controllers;

import exceptions.errors.CustomError;
import exceptions.errors.CustomRestExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getBook(@PathVariable(name="bookId")Long bookId) {
        ResponseEntity<Object> response = (ResponseEntity)bookService.getBook(bookId);
        return response;
    }

    @PostMapping("/api/books")
    public ResponseEntity<?> saveBook(@RequestBody Book book){
        String message = "'id' is not allowed to send";
        if(book.getId()!=null){
            CustomError ce = new CustomError(HttpStatus.BAD_REQUEST.value(), message, message);

            CustomRestExceptionHandler creh = new CustomRestExceptionHandler();
            return creh.handleBadRequest(ce);
        }
        try {
            bookService.saveBook(book);
        } catch (DataIntegrityViolationException e){
            String detailError = e.getMostSpecificCause().getMessage();
            String customError = getCustomErrorMessages(detailError);

            CustomError ce = new CustomError(HttpStatus.CONFLICT.value(), detailError, customError);

            CustomRestExceptionHandler creh = new CustomRestExceptionHandler();
            return creh.handleDataIntegrityViolationException(ce);
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
            String message = "'id' is not allowed to send";
            CustomError ce = new CustomError(HttpStatus.BAD_REQUEST.value(), message, message);

            CustomRestExceptionHandler creh = new CustomRestExceptionHandler();
            return creh.handleBadRequest(ce);
        }

        ResponseEntity bookResponse = bookService.getBook(bookId);
        if(bookResponse.getStatusCode()==HttpStatus.NOT_FOUND) {
            return bookResponse;
        }
        else {
            try {
                bookToUpdate = (Book) bookResponse.getBody();
                bookToUpdate.setAuthor(book.getAuthor());
                bookToUpdate.setTitle(book.getTitle());

                bookService.updateBook(bookToUpdate);
                return new ResponseEntity<Book>(bookToUpdate, HttpStatus.OK);
            } catch (DataIntegrityViolationException e){
                String detailError = e.getMostSpecificCause().getMessage();
                String customError = getCustomErrorMessages(detailError);

                CustomError ce = new CustomError(HttpStatus.CONFLICT.value(), detailError, customError);

                CustomRestExceptionHandler creh = new CustomRestExceptionHandler();
                return creh.handleDataIntegrityViolationException(ce);
            }
        }

    }

    private String getCustomErrorMessages(String detailError) {
        int startIndex = detailError.indexOf("ON ")+3;
        int endIndex = detailError.indexOf(" VALUES");
        String schema = detailError.substring(startIndex, endIndex);

        String schemaName = schema.substring(0, schema.indexOf(".")-1);
        String tableName = schema.substring(schema.indexOf(".")+1, schema.indexOf("(")-1);
        String fieldName = schema.substring(schema.indexOf("(")+1,schema.indexOf(")"));

        return "'" + fieldName + "'"+" cannot be duplicate!";
    }

}
