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

package de.flapdoodle.mongoom.examples.readme;

import java.util.List;

import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.mapping.properties.Property;
import de.flapdoodle.mongoom.mapping.properties.PropertyReference;

public class Meta {

	public static final PropertyReference<List<String>> Keywords = Property.ref("keywords",
			Property.listType(String.class));

	@IndexedInGroup(group = "multikey")
	List<String> _keywords;

	// ...

	public List<String> getKeywords() {
		return _keywords;
	}

	public void setKeywords(List<String> keywords) {
		_keywords = keywords;
	}
}
