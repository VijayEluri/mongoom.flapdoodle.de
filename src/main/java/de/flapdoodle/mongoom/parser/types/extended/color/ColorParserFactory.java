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

package de.flapdoodle.mongoom.parser.types.extended.color;

import java.awt.Color;

import de.flapdoodle.mongoom.parser.IMapping;
import de.flapdoodle.mongoom.parser.IMapProperties;
import de.flapdoodle.mongoom.parser.IType;
import de.flapdoodle.mongoom.parser.ITypeParser;
import de.flapdoodle.mongoom.parser.ITypeParserFactory;
import de.flapdoodle.mongoom.parser.mapping.Mapping;


public class ColorParserFactory implements ITypeParserFactory {

	@Override
	public ITypeParser getParser(IType type) {
		if (type.getType().isAssignableFrom(Color.class)) {
			return new ColorParser();
		}
		return null;
	}
	
	static class ColorParser implements ITypeParser {

		@Override
		public void parse(IMapProperties mapping, IType clazz) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
