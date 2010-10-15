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

package de.flapdoodle.mongoom.mapping.naming;

import java.lang.reflect.Field;

import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.IFieldNamingFactory;

public class PrefixFieldNamingFactory implements IFieldNamingFactory
{
	@Override
	public String getEntityName(Field field)
	{
		if (field.getAnnotation(Id.class)!=null)
		{
			throw new MappingException(field.getDeclaringClass(),"No Fieldname resolved for Id. You should never reach this point. Something went wrong.");
		}
		String fieldName=field.getName();
		return fieldName.replace("_", "");
	}
}
