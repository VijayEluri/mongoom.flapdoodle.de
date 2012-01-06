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

package de.flapdoodle.mongoom.mapping;

import java.util.Set;

import de.flapdoodle.mongoom.mapping.properties.PropertyName;
import de.flapdoodle.mongoom.mapping.properties.TypedPropertyName;

public interface ITransformation<Bean,Mapped> {

	Mapped asObject(Bean value);

	Bean asEntity(Mapped object);
	
	<Source> PropertyName<Source> propertyName(TypedPropertyName<Source> property);
	PropertyName<?> propertyName(String property);
	<Source> ITransformation<Source,?> propertyTransformation(PropertyName<Source> property);
	
//	<Source> ITransformation<Source,?> propertyTransformation(TypedPropertyName<Source> property);
//	ITransformation<?,?> propertyTransformation(String property);
	
	Set<PropertyName<?>> properties();
}
