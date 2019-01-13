package io.knowledgeassets.myskills.server.listener;

import io.knowledgeassets.myskills.server.neo4j.IndexManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OnApplicationStartupListener {

	private final IndexManager indexManager;

	@Autowired
	public OnApplicationStartupListener(IndexManager indexManager) {
		this.indexManager = indexManager;
	}

	@EventListener
	public void createNeo4jIndexes(ApplicationStartedEvent event) {
		indexManager.createIndexes();
	}
}
