package io.knowledgeassets.myskills.server.neo4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.neo4j.ogm.session.SessionFactory;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class IndexServiceTest {

	@Mock
	private SessionFactory sessionFactory;
	private IndexService service;

	@BeforeEach
	public void setup() {
		service = new IndexService(sessionFactory);
	}

	@Test
	public void parseWrongIndexTest() {
		assertThat(service.parseIndexToCreateStatement("some wrong string")).isNull();
	}

	@Test
	public void parseCompositeIndexTest() {
		assertThat(service.parseIndexToCreateStatement("INDEX ON :Person(age, country)"))
				.isEqualTo("CREATE INDEX ON :`Person`(`age`,`country`)");
	}

	@Test
	public void parseSingleIndexTest() {
		assertThat(service.parseIndexToCreateStatement("INDEX ON :Person(firstname)"))
				.isEqualTo("CREATE INDEX ON :`Person`(`firstname`)");
	}

	@Test
	public void parseUniqueConstraintTest() {
		assertThat(service.parseIndexToCreateStatement("CONSTRAINT ON (book:Book) ASSERT book.isbn IS UNIQUE"))
				.isEqualTo("CREATE CONSTRAINT ON (`book`:`Book`) ASSERT `book`.`isbn` IS UNIQUE");
	}

	@Test
	public void parseNodeKeyConstraintTest() {
		assertThat(service.parseIndexToCreateStatement("CONSTRAINT ON (person:Person) ASSERT (person.firstname, person.surname) IS NODE KEY"))
				.isEqualTo("CREATE CONSTRAINT ON (`person`:`Person`) ASSERT (`person`.`firstname`,`person`.`surname`) IS NODE KEY");
	}

	@Test
	public void parseNodePropertyExistenceConstraintTest() {
		assertThat(service.parseIndexToCreateStatement("CONSTRAINT ON (book:Book) ASSERT exists(book.isbn)"))
				.isEqualTo("CREATE CONSTRAINT ON (`book`:`Book`) ASSERT exists(`book`.`isbn`)");
	}

	@Test
	public void parseRelationshipPropertyExistenceConstraintTest() {
		assertThat(service.parseIndexToCreateStatement("CONSTRAINT ON ()-[liked:LIKED]-() ASSERT exists(liked.day)"))
				.isEqualTo("CREATE CONSTRAINT ON ()-[`liked`:`LIKED`]-() ASSERT exists(`liked`.`day`)");
	}
}
