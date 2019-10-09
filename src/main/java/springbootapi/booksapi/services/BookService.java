package springbootapi.booksapi.services;


import org.springframework.http.ResponseEntity;
import springbootapi.booksapi.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> retrieveBooks();

    ResponseEntity<?> getBook(Long bookId);

    void saveBook(Book book);

    void deleteBook(Long bookId);

    void updateBook(Book book);
}
