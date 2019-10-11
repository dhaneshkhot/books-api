package springbootapi.booksapi.services.impl;

import exceptions.CustomRestExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import responses.CustomError;
import responses.CustomSuccess;
import springbootapi.booksapi.entity.Book;
import springbootapi.booksapi.repository.BookRepository;
import springbootapi.booksapi.services.BookService;


import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> retrieveBooks() {
        List<Book> books = bookRepository.findAll();
        return books;
    }

    @Override
    public ResponseEntity<?> getBook(Long bookId) {
        Optional<Book> optBook = bookRepository.findById(bookId);
        if (!optBook.isPresent()) {
            CustomSuccess customSuccess = new CustomSuccess(HttpStatus.NOT_FOUND.value(), "Book not found for id: '" + bookId);
            return new ResponseEntity<CustomSuccess>(customSuccess, HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<Book>(optBook.get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> saveBook(Book book) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        if(book.getId() != null){
            ResponseEntity bookResponse = getBook(book.getId());
            if(bookResponse.getStatusCode()==HttpStatus.NOT_FOUND) {
                return bookResponse;
            }
            httpStatus = HttpStatus.OK;
        }
        try {
            bookRepository.save(book);
        } catch (DataIntegrityViolationException e){
            String detailError = e.getMostSpecificCause().getMessage();
            String customError = getCustomErrorMessages(detailError);

            CustomError ce = new CustomError(HttpStatus.CONFLICT.value(), detailError, customError);

            CustomRestExceptionHandler creh = new CustomRestExceptionHandler();
            return creh.handleDataIntegrityViolationException(ce);
        } catch (Exception e) {
            String message = "Server error";
            CustomError error = new CustomError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), message);
            CustomRestExceptionHandler creh = new CustomRestExceptionHandler();
            return creh.handleInternalServerError(error);
        }
        return new ResponseEntity<Book>(book, httpStatus);
    }

    @Override
    public ResponseEntity<?> deleteBook(Long bookId) {
        try{
        ResponseEntity<?> response = getBook(bookId);
        if(response.getStatusCode().value() == 404)
            return response;
        bookRepository.deleteById(bookId);
        } catch(EmptyResultDataAccessException erdae){
            CustomError error = new CustomError(HttpStatus.BAD_REQUEST.value(), erdae.getMessage(), "No book to delete");

            CustomRestExceptionHandler creh = new CustomRestExceptionHandler();
            return creh.handleInternalServerError(error);
        }
        catch (Exception e) {
            String message = "Server error";
            CustomError error = new CustomError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), message);
            CustomRestExceptionHandler creh = new CustomRestExceptionHandler();
            return creh.handleInternalServerError(error);
        }
        CustomSuccess customSuccess = new CustomSuccess(HttpStatus.NO_CONTENT.value(), "Book deleted: " + bookId);
        return new ResponseEntity<CustomSuccess>(customSuccess, HttpStatus.NO_CONTENT);
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
