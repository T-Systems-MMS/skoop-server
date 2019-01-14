package io.knowledgeassets.myskills.server.neo4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class IndexManager {

	private final List<String> fileIndexes;
	private final IndexService indexService;

	@Autowired
	public IndexManager(IndexService indexService,
						@Value("${spring.data.neo4j.dump-dir}") String dumpDirectory,
						@Value("${spring.data.neo4j.dump-filename}") String dumpFileName) throws IOException {
		fileIndexes = loadIndexesFromFile(dumpDirectory, dumpFileName);
		this.indexService = indexService;
	}

	public void createIndexes() {
		List<String> dbIndexes = indexService.loadIndexesFromDB();

		List<String> createStatements = new ArrayList<>();
		for (String index : fileIndexes) {
			if (!dbIndexes.contains(index)) {
				createStatements.add(index);
			}
		}

		if (!createStatements.isEmpty()) {
			indexService.executeStatements(createStatements);
		}
	}

	public List<String> getFileIndexes() {
		return Collections.unmodifiableList(fileIndexes);
	}

	private List<String> loadIndexesFromFile(String dumpDirectory, String dumpFileName) throws IOException {
		Path dumpPath = Paths.get(dumpDirectory, dumpFileName);
		return Files.readAllLines(dumpPath);
	}

}
