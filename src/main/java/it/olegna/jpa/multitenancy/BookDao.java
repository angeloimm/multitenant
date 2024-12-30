package it.olegna.jpa.multitenancy;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDao extends JpaRepository<Book, Long> {
  static Book titleAndIsbn(String title, String isbn) {
    Book book = new Book();
    book.setTitle(title);
    book.setIsbn(isbn);
    return book;
  }
}
