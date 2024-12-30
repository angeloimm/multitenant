package it.olegna.jpa.multitenancy;

import static it.olegna.jpa.multitenancy.DatabaseRouter.DBA;
import static it.olegna.jpa.multitenancy.DatabaseRouter.DBB;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/books")
public class BookController {
  private BookDao bookDao;
  private TenantIdentifierResolver tenantIdentifierResolver;
  private TransactionTemplate txTemplate;

  @GetMapping
  public ResponseEntity<List<Book>> getBooks() {
    return ResponseEntity.ok(bookDao.findAll());
  }

  @GetMapping(value = "{isbn}")
  public ResponseEntity<Book> getBook(@PathVariable(name = "isbn") String isbn) {

    tenantIdentifierResolver.setCurrentTenant(DBA);
    Book book = bookDao.findByIsbn(isbn);
    if (book == null) {
      log.info("No book with isbn {} in DBA; switching to DBB", isbn);
      tenantIdentifierResolver.setCurrentTenant(DBB);
      book = bookDao.findByIsbn(isbn);
    }
    return ResponseEntity.ok(book);
  }
  @PostConstruct
  public void init(){
    createBook(DBA, "The lord of the rings", "isbn-a-post-construct");
    createBook(DBB, "The Pillars of the Earth", "isbn-b-post-construct");
  }
  private Book createBook(String schema, String title, String isbn) {

    tenantIdentifierResolver.setCurrentTenant(schema);

    Book book = txTemplate.execute(tx -> {
      Book bookObj = BookDao.titleAndIsbn(title, isbn);
      return bookDao.save(bookObj);
    });
    return book;
  }
}
