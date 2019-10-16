package springbootapi.booksapi.unit.services;

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
    public void booksShouldNotBeEmpty(){
        ResponseEntity response = bookService.retrieveBooks();
        Assert.assertEquals("failure, Status code did not match ", HttpStatus.OK, response.getStatusCode());
        List<Book> books = (List<Book>)response.getBody();
        Assert.assertTrue("", books.size() == 3);
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
        Book book = new Book();
        book.setAuthor("Author");
        book.setTitle("Title");

        ResponseEntity<?> savedBookResponse = bookService.saveBook(book);
        Assert.assertEquals(HttpStatus.CREATED, savedBookResponse.getStatusCode());

        Book savedBook = (Book)savedBookResponse.getBody();
        Assert.assertEquals("Author", savedBook.getAuthor());
        Assert.assertEquals("Title", savedBook.getTitle());
    }

    @Test
    public void shouldBeAbleToUpdateBook(){
        Book book = new Book();
        book.setId(1l);
        book.setAuthor("AuthorUpdated");
        book.setTitle("TitleUpdated");

        ResponseEntity<?> updatedBookResponse = bookService.saveBook(book);
        Assert.assertEquals(HttpStatus.OK, updatedBookResponse.getStatusCode());

        Book updatedBook = (Book)updatedBookResponse.getBody();
        Assert.assertEquals("AuthorUpdated", updatedBook.getAuthor());
        Assert.assertEquals("TitleUpdated", updatedBook.getTitle());

    }

    @Test
    public void shouldBeAbleToDeleteBook(){
        ResponseEntity<?> deletedBookResponse = bookService.deleteBook(2l);
        Assert.assertEquals(HttpStatus.NO_CONTENT, deletedBookResponse.getStatusCode());
    }

    @Test
    public void shouldReturnConflictResponseOnCreateExistentBookAuthor(){
        Book book = new Book();
        book.setAuthor("Author3");
        book.setTitle("Title3");
        ResponseEntity<?> savedBookConflictResponse = bookService.saveBook(book);

        Assert.assertEquals(HttpStatus.CONFLICT, savedBookConflictResponse.getStatusCode());

        CustomError error = (CustomError)savedBookConflictResponse.getBody();
        Assert.assertEquals("'AUTHOR' cannot be duplicate!", error.getError());
    }

    @Test
    public void shouldReturnConflictResponseOnCreateExistentBookTitle(){
        Book book = new Book();
        book.setAuthor("Author31");
        book.setTitle("Title3");
        ResponseEntity<?> savedBookConflictResponse = bookService.saveBook(book);

        Assert.assertEquals(HttpStatus.CONFLICT, savedBookConflictResponse.getStatusCode());

        CustomError error = (CustomError)savedBookConflictResponse.getBody();
        Assert.assertEquals("'TITLE' cannot be duplicate!", error.getError());
    }

}
