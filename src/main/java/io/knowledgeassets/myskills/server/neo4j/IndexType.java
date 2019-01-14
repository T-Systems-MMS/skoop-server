package io.knowledgeassets.myskills.server.neo4j;

import java.util.Arrays;

public enum IndexType {

	/**
	 * Single property neo4j
	 */
	SINGLE_INDEX {
		@Override
		public String indexCommand(String owningType, String[] properties) {
			if (properties.length != 1) {
				throw new IllegalArgumentException(SINGLE_INDEX + " must have exactly one property, got " +
						Arrays.toString(properties));
			}
			return "INDEX ON :`" + owningType + "`(`" + properties[0] + "`)";
		}
	},

	/**
	 * Composite neo4j
	 */
	COMPOSITE_INDEX {
		@Override
		public String indexCommand(String owningType, String[] properties) {
			StringBuilder sb = new StringBuilder();
			sb.append("INDEX ON :`")
					.append(owningType)
					.append("`(");
			appendProperties(sb, properties);
			sb.append(")");
			return sb.toString();
		}
	},

	/**
	 * Unique constraint
	 */
	UNIQUE_CONSTRAINT {
		@Override
		public String indexCommand(String owningType, String[] properties) {
			String name = owningType.toLowerCase();

			if (properties.length != 1) {
				throw new IllegalArgumentException(UNIQUE_CONSTRAINT + " must have exactly one property, got " +
						Arrays.toString(properties));
			}
			return "CONSTRAINT ON (`" + name + "`:`" + owningType + "`) ASSERT `" + name + "`.`" + properties[0]
					+ "` IS UNIQUE";
		}
	},

	/**
	 *
	 */
	NODE_KEY_CONSTRAINT {
		@Override
		public String indexCommand(String owningType, String[] properties) {
			String name = owningType.toLowerCase();

			StringBuilder sb = new StringBuilder();
			sb.append("CONSTRAINT ON (`")
					.append(name)
					.append("`:`")
					.append(owningType)
					.append("`) ASSERT (");
			appendPropertiesWithNode(sb, name, properties);
			sb.append(") IS NODE KEY");
			return sb.toString();
		}
	},

	/**
	 * Node property existence constraint
	 */
	NODE_PROP_EXISTENCE_CONSTRAINT {
		@Override
		public String indexCommand(String owningType, String[] properties) {
			String name = owningType.toLowerCase();

			if (properties.length != 1) {
				throw new IllegalArgumentException(
						NODE_PROP_EXISTENCE_CONSTRAINT + " must have exactly one property, got " +
								Arrays.toString(properties));
			}
			return "CONSTRAINT ON (`" + name + "`:`" + owningType + "`) ASSERT exists(`" + name + "`.`"
					+ properties[0] + "`)";
		}
	},

	/**
	 * Relationship property existence constraint
	 */
	REL_PROP_EXISTENCE_CONSTRAINT {
		@Override
		public String indexCommand(String owningType, String[] properties) {
			String name = owningType.toLowerCase();

			if (properties.length != 1) {
				throw new IllegalArgumentException(
						NODE_PROP_EXISTENCE_CONSTRAINT + " must have exactly one property, got " +
								Arrays.toString(properties));
			}
			return "CONSTRAINT ON ()-[`" + name + "`:`" + owningType + "`]-() ASSERT exists(`" + name + "`.`"
					+ properties[0] + "`)";
		}
	};

	public String convertToCreateCommand(String owningType, String[] properties) {
		return "CREATE " + indexCommand(owningType, properties);
	}

	protected abstract String indexCommand(String owningType, String[] properties);

	private static void appendProperties(StringBuilder sb, String[] properties) {
		for (int i = 0; i < properties.length; i++) {
			sb.append('`');
			sb.append(properties[i]);
			sb.append('`');
			if (i < (properties.length - 1)) {
				sb.append(',');
			}
		}
	}

	private static void appendPropertiesWithNode(StringBuilder sb, String nodeName, String[] properties) {
		for (int i = 0; i < properties.length; i++) {
			sb.append('`');
			sb.append(nodeName);
			sb.append("`.`");
			sb.append(properties[i]);
			sb.append('`');
			if (i < (properties.length - 1)) {
				sb.append(',');
			}
		}
	}

}
