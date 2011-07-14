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

package de.flapdoodle.mongoom.parser.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.parser.IFieldType;
import de.flapdoodle.mongoom.parser.IMappedProperty;
import de.flapdoodle.mongoom.parser.IMapProperties;
import de.flapdoodle.mongoom.parser.IType;

public abstract class AbstractPropertyMapping<T extends IType> implements IMapProperties {

	private final T _type;
	Map<String, IMappedProperty> _properties = Maps.newLinkedHashMap();

	protected AbstractPropertyMapping(T type) {
		_type = type;
	}

	protected void error(String message) {
		throw new MappingException(_type.getType(), message);
	}

	@Override
	public T getType() {
		return _type;
	}

	@Override
	public IMappedProperty newProperty(IFieldType type) {
		String name=type.getName();
		if (_properties.containsKey(name))
			throw new MappingException(_type.getType(), "Property " + name + " allready mapped");
		FieldMapping ret = new FieldMapping(type);
		_properties.put(name, ret);
		return ret;
	}
	
	@Override
	public IMappedProperty newProperty(IFieldType type, IMappedProperty proxy) {
		String name=type.getName();
		if (_properties.containsKey(name))
			throw new MappingException(_type.getType(), "Property " + name + " allready mapped");
		MappedPropertyProxy ret = new MappedPropertyProxy(type,proxy);
		_properties.put(name, ret);
		return ret;
	}
	
	
	protected Collection<IMappedProperty> getProperties() {
		return _properties.values();
	}

	@Override
	public String toString() {
		if (!_properties.isEmpty())
			return "properties=" + _properties;
		return "";
	}

}