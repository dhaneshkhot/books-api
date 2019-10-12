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

        // Create some test data
        Long id = 1l;
        Book entity = getEntityStubData();

        // Stub the BookService.getBook method return value
        when(bookService.getBook(id)).thenReturn(new ResponseEntity(entity, HttpStatus.OK));

        // Perform the behavior being tested
        String uri = "/api/books/{id}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Book book = mapFromJson(content, Book.class);

        // Verify the GreetingService.findOne method was invoked once
        verify(bookService, times(1)).getBook(id);

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertEquals("failure, Author did not match: ", entity.getAuthor(), book.getAuthor());
        Assert.assertEquals("failure, Title did not match: ", entity.getTitle(), book.getTitle());
    }

    private Book getEntityStubData() {
        Book entity = new Book();
        entity.setId(1L);
        entity.setTitle("MockTitle");
        entity.setAuthor("MockAuthor");
        return entity;
    }

}
