package io.knowledgeassets.myskills.server.neo4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.neo4j.ogm.session.SessionFactory;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class IndexManagerTest {

	@Mock
	private SessionFactory sessionFactory;

	private IndexManager indexManager;

	@BeforeEach
	public void setUp() throws IOException {
		indexManager = new IndexManager(sessionFactory, "migration/generated_indexes.cql");
	}

	@Test
	public void testThrowNoSuchFileException() {
		assertThrows(NoSuchFileException.class,
				() -> new IndexManager(sessionFactory, "wrong/path/to/file/wrong_filename.cql"));
	}

	@Test
	public void parseWrongIndexTest() {
		assertThat(indexManager.parseIndexToCreateStatement("some wrong string")).isNull();
	}

	@Test
	public void parseCompositeIndexTest() {
		assertThat(indexManager.parseIndexToCreateStatement("INDEX ON :Person(age, country)"))
				.isEqualTo("CREATE INDEX ON :`Person`(`age`,`country`)");
	}

	@Test
	public void parseSingleIndexTest() {
		assertThat(indexManager.parseIndexToCreateStatement("INDEX ON :Person(firstname)"))
				.isEqualTo("CREATE INDEX ON :`Person`(`firstname`)");
	}

	@Test
	public void parseUniqueConstraintTest() {
		assertThat(indexManager.parseIndexToCreateStatement("CONSTRAINT ON (book:Book) ASSERT book.isbn IS UNIQUE"))
				.isEqualTo("CREATE CONSTRAINT ON (`book`:`Book`) ASSERT `book`.`isbn` IS UNIQUE");
	}

	@Test
	public void parseNodeKeyConstraintTest() {
		assertThat(indexManager.parseIndexToCreateStatement("CONSTRAINT ON (person:Person) ASSERT (person.firstname, person.surname) IS NODE KEY"))
				.isEqualTo("CREATE CONSTRAINT ON (`person`:`Person`) ASSERT (`person`.`firstname`,`person`.`surname`) IS NODE KEY");
	}

	@Test
	public void parseNodePropertyExistenceConstraintTest() {
		assertThat(indexManager.parseIndexToCreateStatement("CONSTRAINT ON (book:Book) ASSERT exists(book.isbn)"))
				.isEqualTo("CREATE CONSTRAINT ON (`book`:`Book`) ASSERT exists(`book`.`isbn`)");
	}

	@Test
	public void parseRelationshipPropertyExistenceConstraintTest() {
		assertThat(indexManager.parseIndexToCreateStatement("CONSTRAINT ON ()-[liked:LIKED]-() ASSERT exists(liked.day)"))
				.isEqualTo("CREATE CONSTRAINT ON ()-[`liked`:`LIKED`]-() ASSERT exists(`liked`.`day`)");
	}

}
