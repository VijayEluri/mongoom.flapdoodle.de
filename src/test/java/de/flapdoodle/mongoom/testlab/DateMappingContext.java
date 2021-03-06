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

package de.flapdoodle.mongoom.testlab;

import java.awt.Color;
import java.util.Date;

import de.flapdoodle.mongoom.mapping.ITypeInfo;
import de.flapdoodle.mongoom.mapping.ITypeVisitor;
import de.flapdoodle.mongoom.mapping.context.MappingContext;
import de.flapdoodle.mongoom.mapping.types.color.ColorVisitor;
import de.flapdoodle.mongoom.mapping.types.date.DateVisitor;


public class DateMappingContext extends MappingContext {
	@Override
	public <Type> ITypeVisitor<Type, ?> getVisitor(ITypeInfo containerType, ITypeInfo type) {
		if (type.getType()==Date.class) {
			return (ITypeVisitor<Type, ?>) new DateVisitor();
		}
		return super.getVisitor(containerType, type);
	}
}