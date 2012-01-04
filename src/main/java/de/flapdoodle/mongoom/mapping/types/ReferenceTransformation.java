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

package de.flapdoodle.mongoom.mapping.types;

import java.util.Set;

import org.bson.types.ObjectId;

import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.properties.TypedPropertyName;
import de.flapdoodle.mongoom.types.Reference;


public class ReferenceTransformation<R> implements ITransformation<Reference<R>,ObjectId> {

	
	private final Class<R> _type;

	public ReferenceTransformation(Class<R> type) {
		_type = type;
	}
	
	@Override
	public ObjectId asObject(Reference<R> value) {
		return value!=null ? value.getId() : null;
	}

	@Override
	public Reference<R> asEntity(ObjectId object) {
		return object!=null ? Reference.getInstance(_type, (ObjectId) object) : null;
	}

	@Override
	public <S> ITransformation<S,?> propertyTransformation(TypedPropertyName<S> property) {
		throw new MappingException(_type,"Reference has no Properties");
	}

	@Override
	public ITransformation<?, ?> propertyTransformation(String property) {
		throw new MappingException(_type,"Reference has no Properties");
	}
	
	@Override
	public Set<TypedPropertyName<?>> properties() {
		return Sets.newHashSet();
	}
	
}