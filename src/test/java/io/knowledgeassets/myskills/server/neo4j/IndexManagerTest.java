package io.knowledgeassets.myskills.server.neo4j;

import io.knowledgeassets.myskills.server.common.Neo4jSessionFactoryConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(SpringExtension.class)
@Import({Neo4jSessionFactoryConfiguration.class})
public class IndexManagerTest {

	@Autowired
	private SessionFactory sessionFactory;
	@MockBean
	private IndexService mockIndexService;

	private IndexManager indexManager;

	@BeforeEach
	public void setUp() {
		initMocks(this);

		indexManager = new IndexManager(sessionFactory, mockIndexService);
	}

	@Test
	public void testCreateNewIndexes() {
		doReturn(Collections.emptyList()).when(mockIndexService).loadIndexesFromDB();
		indexManager.createIndexes();

		verify(mockIndexService).executeStatements(any());
	}

	@Test
	public void testDoNothingIfIndexesAlreadyExist() {
		doReturn(indexManager.getIndexes()).when(mockIndexService).loadIndexesFromDB();
		indexManager.createIndexes();

		verify(mockIndexService, never()).executeStatements(any());
	}

}
