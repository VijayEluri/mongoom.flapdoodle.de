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

package de.flapdoodle.mongoom.testlab.datastore;

import java.util.List;

import com.google.common.collect.Lists;

import de.flapdoodle.mongoom.AbstractMongoOMTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.testlab.datastore.beans.Book;
import de.flapdoodle.mongoom.testlab.mapping.MappingContext;
import de.flapdoodle.mongoom.testlab.mapping.Transformations;

import junit.framework.TestCase;


public class TestDatastore extends AbstractMongoOMTest {

	public void testDatastore() {
		List<Class<?>> classes=Lists.newArrayList();
		classes.add(Book.class);
		Transformations transformations = new Transformations(MappingContext.factory(),classes);
		IDatastore datastore=new Datastore(getMongo(), getDatabaseName(), transformations);
		Book book = new Book();
		book.setName("Bla");
		datastore.save(book);
		book.setName("Blu");
		datastore.update(book);
		book=new Book();
		book.setName("2. Buch");
		datastore.save(book);
		
		List<Book> books = datastore.find(Book.class);
		assertEquals("Size",2,books.size());
//		System.out.println("Books: "+books);
	}
	
	
}
