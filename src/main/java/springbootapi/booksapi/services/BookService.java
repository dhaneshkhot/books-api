package springbootapi.booksapi.services;


import org.springframework.http.ResponseEntity;
import springbootapi.booksapi.entity.Book;

public interface BookService {
    ResponseEntity<?> retrieveBooks();

    ResponseEntity<?> getBook(Long bookId);

    ResponseEntity<?> saveBook(Book book);

    ResponseEntity<?> deleteBook(Long bookId);
}
