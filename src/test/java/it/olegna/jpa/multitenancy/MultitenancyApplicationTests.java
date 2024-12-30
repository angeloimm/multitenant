package it.olegna.jpa.multitenancy;

import static it.olegna.jpa.multitenancy.DatabaseRouter.DBA;
import static it.olegna.jpa.multitenancy.DatabaseRouter.DBB;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.support.TransactionTemplate;


@SpringBootTest
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
class MultitenancyApplicationTests {

	@Autowired BookDao persons;

	@Autowired
	TransactionTemplate txTemplate;

	@Autowired
	TenantIdentifierResolver currentTenant;

	@Test
	void saveAndLoadBook() {

		createBook(DBA, "The lord of the rings", "isbn-a");
		createBook(DBB, "The Pillars of the Earth", "isbn-b");

		currentTenant.setCurrentTenant(DBA);
		assertThat(persons.findAll()).extracting(Book::getIsbn).containsExactly("isbn-a");

		currentTenant.setCurrentTenant(DBB);
		assertThat(persons.findAll()).extracting(Book::getIsbn).containsExactly("isbn-b");
	}

	private Book createBook(String schema, String title, String isbn) {

		currentTenant.setCurrentTenant(schema);

		Book book = txTemplate.execute(tx -> {
			Book bookObj = BookDao.titleAndIsbn(title, isbn);
			return persons.save(bookObj);
		});
		assertThat(book.getId()).isNotNull();
		return book;
	}

}
