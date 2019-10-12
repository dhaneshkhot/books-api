package springbootapi.booksapi.services.impl;

import errors.CustomRestErrorResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
            return CustomRestErrorResponseHandler.handleNotFoundException(bookId);
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
            return CustomRestErrorResponseHandler.handleDataIntegrityViolationException(e);
        } catch (Exception e) {
            return CustomRestErrorResponseHandler.handleInternalServerError(e);
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
        } catch (Exception e) {
            return CustomRestErrorResponseHandler.handleInternalServerError(e);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
