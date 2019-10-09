package springbootapi.booksapi.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
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
            return new ResponseEntity<Object>("Book not found for id '" + bookId + "'", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<Book>(optBook.get(), HttpStatus.OK);
    }

    @Override
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public void updateBook(Book book) {
        bookRepository.save(book);
    }
}
