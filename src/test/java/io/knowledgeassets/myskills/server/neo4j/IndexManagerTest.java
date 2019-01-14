package io.knowledgeassets.myskills.server.neo4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
	public void testThrowNoSuchFileException() {
		assertThrows(NoSuchFileException.class,
				()-> new IndexManager(mockIndexService, "wrong/path/to/file", "wrong_filename.cql"));
	}

	@Test
	public void testCreateNewIndexes() {
		List<String> dbIndexes = new ArrayList<>(indexManager.getFileIndexes());
		String newIndex = dbIndexes.remove(0);
		doReturn(dbIndexes).when(mockIndexService).loadIndexesFromDB();
		indexManager.createIndexes();

		verify(mockIndexService).executeStatements(Collections.singletonList(newIndex));
	}

	@Test
	public void testDoNothingIfIndexesAlreadyExist() {
		doReturn(indexManager.getFileIndexes()).when(mockIndexService).loadIndexesFromDB();
		indexManager.createIndexes();

		verify(mockIndexService, never()).executeStatements(any());
	}

}
