package springbootapi.booksapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springbootapi.booksapi.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
}
