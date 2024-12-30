package it.olegna.jpa.multitenancy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Book {

  @Id
  @GeneratedValue
  private Long id;
  private String title;
  private String isbn;
  public String toString() {
    return "Book [id=" + id + ", title=" + title + ", isbn=" + isbn + "]";
  }
}
