package io.knowledgeassets.myskills.server.neo4j;

import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.metadata.ClassInfo;
import org.neo4j.ogm.metadata.FieldInfo;
import org.neo4j.ogm.metadata.MetaData;
import org.neo4j.ogm.request.Statement;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class IndexManager {

	private final List<AutoIndex> indexes;
	private final IndexService indexService;

	@Autowired
	public IndexManager(SessionFactory sessionFactory, IndexService indexService) {
		indexes = initialiseAutoIndex(sessionFactory.metaData());
		this.indexService = indexService;
	}

	public void createIndexes() {
		List<AutoIndex> dbIndexes = indexService.loadIndexesFromDB();

		List<Statement> createStatements = new ArrayList<>();
		for (AutoIndex index : indexes) {
			if (!dbIndexes.contains(index)) {
				createStatements.add(index.getCreateStatement());
			}
		}

		if (!createStatements.isEmpty()) {
			indexService.executeStatements(createStatements);
		}
	}

	public List<AutoIndex> getIndexes() {
		return Collections.unmodifiableList(indexes);
	}

	private static List<AutoIndex> initialiseAutoIndex(MetaData metaData) {
		List<AutoIndex> indexMetadata = new ArrayList<>();
		for (ClassInfo classInfo : metaData.persistentEntities()) {

			final String owningType = classInfo.neo4jName();

			if (needsToBeIndexed(classInfo)) {
				for (FieldInfo fieldInfo : getIndexFields(classInfo)) {
					IndexType type = fieldInfo.isConstraint() ? IndexType.UNIQUE_CONSTRAINT : IndexType.SINGLE_INDEX;
					final AutoIndex autoIndex = new AutoIndex(type, owningType,
							new String[]{fieldInfo.property()});
					indexMetadata.add(autoIndex);
				}

				for (CompositeIndex index : classInfo.getCompositeIndexes()) {
					IndexType type = index.unique() ? IndexType.NODE_KEY_CONSTRAINT : IndexType.COMPOSITE_INDEX;
					String[] properties = index.value().length > 0 ? index.value() : index.properties();
					AutoIndex autoIndex = new AutoIndex(type, owningType, properties);
					indexMetadata.add(autoIndex);
				}
			}

			if (classInfo.hasRequiredFields()) {
				for (FieldInfo requiredField : classInfo.requiredFields()) {
					IndexType type = classInfo.isRelationshipEntity() ?
							IndexType.REL_PROP_EXISTENCE_CONSTRAINT : IndexType.NODE_PROP_EXISTENCE_CONSTRAINT;

					AutoIndex autoIndex = new AutoIndex(type, owningType,
							new String[]{requiredField.property()});

					indexMetadata.add(autoIndex);
				}
			}
		}
		return indexMetadata;
	}

	private static boolean needsToBeIndexed(ClassInfo classInfo) {
		return (!classInfo.isAbstract() || classInfo.neo4jName() != null) && containsIndexesInHierarchy(classInfo);
	}

	private static boolean containsIndexesInHierarchy(ClassInfo classInfo) {
		boolean containsIndexes = false;
		ClassInfo currentClassInfo = classInfo;
		while (!containsIndexes && currentClassInfo != null) {
			containsIndexes = currentClassInfo.containsIndexes();
			currentClassInfo = currentClassInfo.directSuperclass();
		}
		return containsIndexes;
	}

	private static List<FieldInfo> getIndexFields(ClassInfo classInfo) {
		List<FieldInfo> indexFields = new ArrayList<>();
		ClassInfo currentClassInfo = classInfo.directSuperclass();
		while (currentClassInfo != null) {
			if (!needsToBeIndexed(currentClassInfo)) {
				indexFields.addAll(currentClassInfo.getIndexFields());
			}
			currentClassInfo = currentClassInfo.directSuperclass();
		}

		indexFields.addAll(classInfo.getIndexFields());
		return indexFields;
	}
}
