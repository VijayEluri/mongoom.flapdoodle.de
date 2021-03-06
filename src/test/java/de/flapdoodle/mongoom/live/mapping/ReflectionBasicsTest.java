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

package de.flapdoodle.mongoom.live.mapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import de.flapdoodle.mongoom.live.beans.User;

public class ReflectionBasicsTest extends TestCase {

	public void testConstructor() throws InstantiationException, IllegalAccessException {
		try
		{
			User user = User.class.newInstance();
		}
		catch (IllegalAccessException iax) {
			// default constructor is protected 
		}
	}

	public void testConstructorList() throws IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException {
		Constructor<User>[] constructors = (Constructor<User>[]) User.class.getConstructors();
		for (Constructor<User> c : constructors) {
			if (c.getParameterTypes().length == 0) {
				c.setAccessible(true);
				User user = c.newInstance();
				assertNotNull("newInstance", user);
			}
		}
	}

}
