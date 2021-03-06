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

import java.util.logging.Logger;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.AbstractClassFieldVisitor;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.ITypeInfo;
import de.flapdoodle.mongoom.mapping.ITypeVisitor;
import de.flapdoodle.mongoom.mapping.context.IMappingContext;
import de.flapdoodle.mongoom.mapping.context.IPropertyContext;


public class PojoVisitor<T> extends AbstractClassFieldVisitor<T,DBObject> implements ITypeVisitor<T, DBObject> {

	private static final Logger _logger = LogConfig.getLogger(PojoVisitor.class);
	
	@Override
	public ITransformation<T, DBObject> transformation(IMappingContext mappingContext,
			IPropertyContext<?> parentContext, ITypeInfo field) {
		ITransformation<T, DBObject> result = (ITransformation<T, DBObject>) mappingContext.transformation(field);
		if (result==null) {
			_logger.warning("Recursion detected: "+field);
		
			PojoContext<T> rootContext=new PojoContext<T>(parentContext, (Class<T>) field.getType());
			parseProperties(mappingContext, rootContext, field);
		
			result=new PojoTransformation<T>(rootContext);
			mappingContext.setTransformation(field, result);
		}
		return result;
	}

}
