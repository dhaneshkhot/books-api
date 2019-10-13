package springbootapi.booksapi.unit.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import springbootapi.booksapi.controllers.BooksController;
import springbootapi.booksapi.entity.Book;
import springbootapi.booksapi.services.BookService;
import springbootapi.booksapi.unit.AbstractTest;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Transactional
@WebAppConfiguration
public class BookControllerMockTest extends AbstractTest {
    protected MockMvc mvc; // simulates http interactions

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BooksController booksController;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(booksController).build();
    }

    @Test
    public void testGetBook() throws Exception {
        Long id = 1l;
        Book bookStubData = getBookStubData();

        // Stub the BookService.getBook method return value
        when(bookService.getBook(id)).thenReturn(new ResponseEntity(bookStubData, HttpStatus.OK));

        // Perform the behavior being tested
        String uri = "/api/books/{id}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Book book = mapFromJson(content, Book.class);

        // Verify the bookService.getBook method was invoked once
        verify(bookService, times(1)).getBook(id);

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertEquals("failure, Author did not match: ", bookStubData.getAuthor(), book.getAuthor());
        Assert.assertEquals("failure, Title did not match: ", bookStubData.getTitle(), book.getTitle());
    }

    @Test
    public void testGetBooks() throws Exception {
        List<Book> booksStubData = getBooksStubData();

        // Stub the BookService.retrieveBooks method return list of books
        when(bookService.retrieveBooks()).thenReturn(booksStubData);

        // Perform the behavior being tested
        String uri = "/api/books";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();

        int status = result.getResponse().getStatus();

        // Verify the bookService.retrieveBooks method was invoked once
        verify(bookService, times(1)).retrieveBooks();

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue("failure, Book list size is not greater than 0: ", content.trim().length() > 0);
    }

    @Test
    public void testBookNotFound() throws Exception {
        Long id = 100l;

        // Stub the bookService.getBook method return value
        when(bookService.getBook(id)).thenReturn(new ResponseEntity(null, HttpStatus.NOT_FOUND));

        // Perform the behavior being tested
        String uri = "/api/books/{id}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        // Verify the bookService.getBook method was invoked once
        verify(bookService, times(1)).getBook(id);

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 404", 404, status);
        Assert.assertTrue("failure - expected HTTP response body to be empty",
                content.trim().length() == 0);

    }

    @Test
    public void testCreateBook() throws Exception {
        // Create some test data
        Book bookStubData = getBookStubData();

        // Stub the bookService.saveBook method return value
        when(bookService.saveBook(any(Book.class))).thenReturn(new ResponseEntity(bookStubData, HttpStatus.CREATED));

        // Perform the behavior being tested
        String uri = "/api/books/";
        String inputJson = mapToJson(bookStubData);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        // Verify the bookService.saveBook method was invoked once
        verify(bookService, times(1)).saveBook(any(Book.class));

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 201", 201, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

        Book createdBook = mapFromJson(content, Book.class);

        Assert.assertNotNull("failure - expected entity not null",
                createdBook);
        Assert.assertNotNull("failure - expected id attribute not null",
                createdBook.getId());
        Assert.assertEquals("failure - expected title attribute match",
                bookStubData.getTitle(), createdBook.getTitle());
        Assert.assertEquals("failure - expected author attribute match",
                bookStubData.getAuthor(), createdBook.getAuthor());
    }

    @Test
    public void testUpdateBook() throws Exception {
        // Create some test data
        Book book = getBookStubData();
        book.setTitle(book.getTitle() + "Updated");
        Long id = 1l;

        // Stub the bookService.saveBook method return value
        when(bookService.saveBook(any(Book.class))).thenReturn(new ResponseEntity(book, HttpStatus.OK));

        // Perform the behavior being tested
        String uri = "/api/books";
        String inputJson = super.mapToJson(book);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        // Verify the bookService.saveBook method was invoked once
        verify(bookService, times(1)).saveBook(any(Book.class));

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

        Book updatedBook = super.mapFromJson(content, Book.class);

        Assert.assertNotNull("failure - expected entity not null",
                updatedBook);
        Assert.assertEquals("failure - expected id attribute unchanged",
                book.getId(), updatedBook.getId());
        Assert.assertEquals("failure - expected author attribute match",
                book.getAuthor(), updatedBook.getAuthor());
        Assert.assertEquals("failure - expected title attribute match",
                book.getTitle(), updatedBook.getTitle());

    }

    @Test
    public void testDeleteBook() throws Exception {
        // Create some test data
        Long id = 1l;

        // Perform the behavior being tested
        String uri = "/api/books/{id}";

        // Stub the bookService.deleteBook method return value
        when(bookService.deleteBook(id)).thenReturn(new ResponseEntity(null, HttpStatus.NO_CONTENT));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, id))
                .andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        // Verify the bookService.deleteBook method was invoked once
        verify(bookService, times(1)).deleteBook(id);

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 204", 204, status);
        Assert.assertTrue("failure - expected HTTP response body to be empty",
                content.trim().length() == 0);

    }

    private Book getBookStubData() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("MockTitle");
        book.setAuthor("MockAuthor");
        return book;
    }

    private List<Book> getBooksStubData() {
        List<Book> books = new ArrayList<Book>();
        for(int i=1; i < 6; i++){
            Book book = new Book();
            Long id = 0l;
            book.setId(id + i);
            book.setTitle("MockTitle" + i);
            book.setAuthor("MockAuthor" + i);
            books.add(book);
        }
        return books;
    }

}
