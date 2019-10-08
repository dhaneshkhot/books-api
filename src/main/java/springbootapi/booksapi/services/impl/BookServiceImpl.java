package springbootapi.booksapi.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Book getBook(Long bookId) {
        Optional<Book> optBook = bookRepository.findById(bookId);
        return optBook.get();
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
