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

package de.flapdoodle.mongoom.mapping.types.date;

import java.util.Date;

import de.flapdoodle.mongoom.mapping.properties.Property;
import de.flapdoodle.mongoom.mapping.properties.PropertyReference;


public class Dates {

	private Dates() {
		throw new IllegalAccessError("singleton");
	}

	public static final PropertyReference<Integer> Year=Property.ref(DateVisitor.YEAR,Integer.class);
	public static final PropertyReference<Integer> Month=Property.ref(DateVisitor.MONTH,Integer.class);
	public static final PropertyReference<Integer> Day=Property.ref(DateVisitor.DAY,Integer.class);
	public static final PropertyReference<Integer> Hour=Property.ref(DateVisitor.HOUR,Integer.class);
	public static final PropertyReference<Integer> Minute=Property.ref(DateVisitor.MINUTE,Integer.class);
	public static final PropertyReference<Integer> Second=Property.ref(DateVisitor.SECOND,Integer.class);
	
	public static final PropertyReference<Date> Time=Property.ref(DateVisitor.TIME,Date.class);

}
