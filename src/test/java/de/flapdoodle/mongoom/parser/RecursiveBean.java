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

package de.flapdoodle.mongoom.parser;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.types.Reference;


@Entity(value = "Recursive")
public class RecursiveBean {

	@Id
	Reference<RecursiveBean> _id;

	@Indexed
	Loop _loop;
	
	NoLoop _noLoop;
	
	static class Loop {

		@Indexed
		Loop _child;
	}
	
	static class NoLoop {
		@Indexed
		Loop _child;
	}
}
