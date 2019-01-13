package io.knowledgeassets.myskills.server.neo4j;

import org.neo4j.ogm.model.RowModel;
import org.neo4j.ogm.request.Statement;
import org.neo4j.ogm.response.Response;
import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.session.request.DefaultRequest;
import org.neo4j.ogm.session.request.RowDataStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static org.neo4j.ogm.transaction.Transaction.Type.READ_WRITE;

@Service
public class IndexService {

	private final Neo4jSession session;

	@Autowired
	public IndexService(SessionFactory sessionFactory) {
		this.session = (Neo4jSession) sessionFactory.openSession();
	}

	public List<AutoIndex> loadIndexesFromDB() {
		DefaultRequest indexRequests = buildProcedures();
		List<AutoIndex> dbIndexes = new ArrayList<>();
		session.doInTransaction(() -> {
			try (Response<RowModel> response = session.requestHandler().execute(indexRequests)) {
				RowModel rowModel;
				while ((rowModel = response.next()) != null) {
					Optional<AutoIndex> dbIndex = AutoIndex.parse((String) rowModel.getValues()[0]);
					dbIndex.ifPresent(dbIndexes::add);
				}
			}
		}, READ_WRITE);

		return dbIndexes;
	}

	public void executeStatements(List<Statement> statements) {
		DefaultRequest request = new DefaultRequest();
		request.setStatements(statements);

		session.doInTransaction(() -> {
			try (Response<RowModel> response = session.requestHandler().execute(request)) {
				// Success
			}
		}, READ_WRITE);
	}

	private DefaultRequest buildProcedures() {
		List<Statement> procedures = new ArrayList<>();

		procedures.add(new RowDataStatement("CALL db.constraints()", emptyMap()));
		procedures.add(new RowDataStatement("call db.indexes() yield description, type with description, type where type <> 'node_unique_property' return description", emptyMap()));

		DefaultRequest getIndexesRequest = new DefaultRequest();
		getIndexesRequest.setStatements(procedures);
		return getIndexesRequest;
	}
}
