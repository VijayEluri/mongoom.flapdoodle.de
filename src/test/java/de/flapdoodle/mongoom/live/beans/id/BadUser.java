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

package de.flapdoodle.mongoom.live.beans.id;

import java.util.List;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Property;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.mapping.properties.PropertyReference;

@Entity("BadUser")
public class BadUser {

	public static final PropertyReference<String> Name=de.flapdoodle.mongoom.mapping.properties.Property.ref("name",String.class);
	
	@Id
	String _id;

	@Property("name")
	String _name;

	@Version
	String _version;

	public static BadUser get(String id, String name) {
		BadUser ret = new BadUser();
		ret._id = id;
		ret._name = name;
		return ret;
	}
}
