package springbootapi.booksapi.java.services;

import errors.CustomError;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import springbootapi.booksapi.entity.Book;
import springbootapi.booksapi.services.impl.BookServiceImpl;
import springbootapi.booksapi.unit.AbstractTest;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class BookServiceImplTest extends AbstractTest {

    @Autowired
    private BookServiceImpl bookService;

    @Before
    public void setup(){

    }

    @Test
    public void booksShouldBeEmpty(){
        List<Book> books = bookService.retrieveBooks();
        Assert.assertEquals(0, books.size());
    }

    @Test
    public void shouldReturnNotFoundOnGetNonExistentID(){
        ResponseEntity response = bookService.getBook(100l);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void shouldReturnNotFoundOnDeleteNonExistentID(){
        ResponseEntity response = bookService.deleteBook(100l);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void shouldCreateBook(){
        Book aBook = new Book();
        aBook.setAuthor("Author");
        aBook.setTitle("Title");

        ResponseEntity<?> savedBookResponse = bookService.saveBook(aBook);
        Assert.assertEquals(HttpStatus.CREATED, savedBookResponse.getStatusCode());

        Book savedBook = (Book)savedBookResponse.getBody();
        Assert.assertEquals("Author", savedBook.getAuthor());
        Assert.assertEquals("Title", savedBook.getTitle());
    }

    @Test
    public void shouldBeAbleToUpdateBook(){
        Book aBook = new Book();
        aBook.setAuthor("Author");
        aBook.setTitle("Title");

        ResponseEntity<?> savedBookResponse = bookService.saveBook(aBook);

        aBook.setId(((Book)savedBookResponse.getBody()).getId());
        aBook.setAuthor("AuthorUpdated");
        aBook.setTitle("TitleUpdated");

        ResponseEntity<?> updatedBookResponse = bookService.saveBook(aBook);
        Assert.assertEquals(HttpStatus.OK, updatedBookResponse.getStatusCode());

        Book updatedBook = (Book)updatedBookResponse.getBody();
        Assert.assertEquals("AuthorUpdated", updatedBook.getAuthor());
        Assert.assertEquals("TitleUpdated", updatedBook.getTitle());

    }

    @Test
    public void shouldBeAbleToDeleteBook(){
        Book aBook = new Book();
        aBook.setAuthor("Author");
        aBook.setTitle("Title");

        ResponseEntity<?> savedBookResponse = bookService.saveBook(aBook);
        ResponseEntity<?> deletedBookResponse = bookService.deleteBook(((Book)savedBookResponse.getBody()).getId());
        Assert.assertEquals(HttpStatus.NO_CONTENT, deletedBookResponse.getStatusCode());
    }

    @Test
    public void shouldReturnConflictResponseOnCreateSameBook(){
        Book aBook = new Book();
        aBook.setAuthor("Author");
        aBook.setTitle("Title");

        ResponseEntity<?> savedBookResponse = bookService.saveBook(aBook);

        Book book2 = new Book();
        book2.setAuthor("Author");
        book2.setTitle("Title");
        ResponseEntity<?> savedBookConflictResponse = bookService.saveBook(book2);

        Assert.assertEquals(HttpStatus.CONFLICT, savedBookConflictResponse.getStatusCode());

        CustomError error = (CustomError)savedBookConflictResponse.getBody();
        Assert.assertEquals("'AUTHOR' cannot be duplicate!", error.getError());
    }

}
