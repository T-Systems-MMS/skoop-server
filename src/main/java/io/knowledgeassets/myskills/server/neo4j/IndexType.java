package io.knowledgeassets.myskills.server.neo4j;

public enum IndexType {

	/**
	 * Single property neo4j
	 */
	SINGLE_INDEX,

	/**
	 * Composite neo4j
	 */
	COMPOSITE_INDEX,

	/**
	 * Unique constraint
	 */
	UNIQUE_CONSTRAINT,

	/**
	 *
	 */
	NODE_KEY_CONSTRAINT,

	/**
	 * Node property existence constraint
	 */
	NODE_PROP_EXISTENCE_CONSTRAINT,

	/**
	 * Relationship property existence constraint
	 */
	REL_PROP_EXISTENCE_CONSTRAINT,

}
