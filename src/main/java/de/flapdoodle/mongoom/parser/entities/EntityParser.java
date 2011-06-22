/**
 * Copyright (C) 2010 Michael Mosmann <michael@mosmann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.flapdoodle.mongoom.parser.entities;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Transient;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroups;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.converter.annotations.Annotations;
import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.mapping.index.IndexParser;
import de.flapdoodle.mongoom.parser.AbstractParser;
import de.flapdoodle.mongoom.parser.FieldType;
import de.flapdoodle.mongoom.parser.IEntityParser;
import de.flapdoodle.mongoom.parser.IType;
import de.flapdoodle.mongoom.parser.ITypeParser;
import de.flapdoodle.mongoom.parser.ITypeParserFactory;
import de.flapdoodle.mongoom.parser.Mapping;

public class EntityParser extends AbstractParser implements IEntityParser {

	private final ITypeParserFactory _typeParserFactory;

	public EntityParser(ITypeParserFactory typeParserFactory) {
		_typeParserFactory = typeParserFactory;
	}

	@Override
	public void parse(Mapping mapping, IType type) {
		Class<?> entityClass=type.getType();
		
		Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
		if (entityAnnotation == null) {
			error(type, "Missing " + Entity.class + " Annotation");
		}
		Map<String, EntityIndexDef> indexGroupMap = IndexParser.getIndexGroupMap(entityClass);

		List<Field> fields = ClassInformation.getFields(entityClass);

		boolean idSet = false;
		boolean isVersioned = false;

		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getAnnotation(Transient.class) == null) {
				Annotations.checkForOnlyOneAnnotation(entityClass, field, Id.class, Indexed.class, IndexedInGroup.class,
						IndexedInGroups.class, Version.class);

				Id idAnn = field.getAnnotation(Id.class);
				Version versionAnn = field.getAnnotation(Version.class);

				if (versionAnn != null) {
					if (isVersioned)
						error(type, "more than one version annotation");
					isVersioned = true;
				}

				if (idAnn != null) {
					if (idSet)
						error(type, "more then one Id annotation");
					idSet = true;
				}

				FieldType fieldType = FieldType.of(field);
				
				ITypeParser parser = _typeParserFactory.getParser(fieldType);
				if (parser==null) error(type,"no parser for "+field);
				parser.parse(mapping, fieldType);
			}
		}
	}

}
