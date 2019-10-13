package springbootapi.booksapi.integration;

import errors.CustomError;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import springbootapi.booksapi.entity.Book;

import java.net.URISyntaxException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BooksApisTests {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testGetBook() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<Book> response = restTemplate.exchange(
                createURLWithPort("/api/books/1"), HttpMethod.GET, entity, Book.class);

        Assert.assertEquals("failure, status code did not match", HttpStatus.OK, response.getStatusCode());

        Book book1 = response.getBody();
        Assert.assertEquals("failure, Author did not match", "Author1", book1.getAuthor());
        Assert.assertEquals("failure, Title did not match", "Title1", book1.getTitle());
    }

    @Test
    public void testGetBookForNotFound() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<CustomError> response = restTemplate.exchange(
                createURLWithPort("/api/books/100"), HttpMethod.GET, entity, CustomError.class);

        Assert.assertEquals("failure, status code did not match", HttpStatus.NOT_FOUND, response.getStatusCode());

        CustomError customError = response.getBody();
        Assert.assertEquals("failure, Error message did not match", "'100' not found!", customError.getError());
    }

    @Test
    public void testGetBooks() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<List<Book>> response = restTemplate.exchange(
                createURLWithPort("/api/books"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>(){});

        Assert.assertEquals("failure, status code did not match", HttpStatus.OK, response.getStatusCode());

        List<Book> books = response.getBody();
        Assert.assertEquals("failure, list size did not match", 3, books.size());
    }

    @Test
    public void testCreateBook() throws URISyntaxException {
        Book book = new Book();
        book.setAuthor("Author11");
        book.setTitle("Title11");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Book> request = new HttpEntity<>(book, headers);

        ResponseEntity<Book> response = this.restTemplate.postForEntity(
                createURLWithPort("/api/books"),
                request,
                Book.class);
        //Verify request succeed
        Assert.assertEquals("failure, status code did not match", 201, response.getStatusCodeValue());

        Book createdBook = response.getBody();
        Assert.assertEquals("failure, Author did not match", book.getAuthor(), createdBook.getAuthor());
        Assert.assertEquals("failure, Title did not match", book.getTitle(), createdBook.getTitle());
    }

    @Test
    public void testCreateConflict() throws Exception {
        Book book = new Book();
        book.setAuthor("Author1");
        book.setTitle("Title1");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Book> request = new HttpEntity<>(book, headers);

        ResponseEntity<CustomError> response = this.restTemplate.postForEntity(
                createURLWithPort("/api/books"),
                request,
                CustomError.class);
        Assert.assertEquals("failure, status code did not match", HttpStatus.CONFLICT, response.getStatusCode());

        CustomError error = response.getBody();
        Assert.assertEquals("failure, error message did not match", "'AUTHOR' cannot be duplicate!", error.getError());

    }

    @Test
    public void testUpdateBook() throws URISyntaxException {
        Book book = new Book();
        book.setId(3l);
        book.setAuthor("Author1Updated");
        book.setTitle("Title1Updated");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Book> request = new HttpEntity<>(book, headers);

        ResponseEntity<Book> response = this.restTemplate.postForEntity(
                createURLWithPort("/api/books"),
                request,
                Book.class);

        Book updatedBook = response.getBody();

        //Verify request succeed
        Assert.assertEquals("failure, status code did not match", 200, response.getStatusCodeValue());
        Assert.assertEquals("failure, Author did not match", book.getAuthor(), updatedBook.getAuthor());
        Assert.assertEquals("failure, Title did not match", book.getTitle(), updatedBook.getTitle());
    }

    @Test
    public void testUpdateConflict() throws Exception {
        Book book = new Book();
        book.setId(2l);
        book.setAuthor("Author1");
        book.setTitle("Title1");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Book> request = new HttpEntity<>(book, headers);

        ResponseEntity<CustomError> response = this.restTemplate.postForEntity(
                createURLWithPort("/api/books"),
                request,
                CustomError.class);
        Assert.assertEquals("failure, status code did not match", HttpStatus.CONFLICT, response.getStatusCode());

        CustomError error = response.getBody();
        Assert.assertEquals("failure, error message did not match", "'AUTHOR' cannot be duplicate!", error.getError());
    }

    @Test
    public void testDeleteBook() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<Object> response = restTemplate.exchange(
                createURLWithPort("/api/books/3"), HttpMethod.DELETE, entity, Object.class);

        Assert.assertEquals("failure, status code did not match", HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteNonExistentBook() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<CustomError> response = restTemplate.exchange(
                createURLWithPort("/api/books/100"), HttpMethod.DELETE, entity, CustomError.class);

        Assert.assertEquals("failure, status code did not match", HttpStatus.NOT_FOUND, response.getStatusCode());

        CustomError customError = response.getBody();
        Assert.assertEquals("failure, Error message did not match", "'100' not found!", customError.getError());

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
