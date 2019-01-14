package io.knowledgeassets.myskills.server.neo4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(SpringExtension.class)
public class IndexManagerTest {

	@MockBean
	private IndexService mockIndexService;

	private IndexManager indexManager;

	@BeforeEach
	public void setUp() throws IOException {
		initMocks(this);

		indexManager = new IndexManager(mockIndexService, "src/main/resources/migration", "generated_indexes.cql");
	}

	@Test
	public void testCreateNewIndexes() {
		doReturn(Collections.emptyList()).when(mockIndexService).loadIndexesFromDB();
		indexManager.createIndexes();

		verify(mockIndexService).executeStatements(any());
	}

	@Test
	public void testDoNothingIfIndexesAlreadyExist() {
		doReturn(indexManager.getFileIndexes()).when(mockIndexService).loadIndexesFromDB();
		indexManager.createIndexes();

		verify(mockIndexService, never()).executeStatements(any());
	}

}
