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

package de.flapdoodle.mongoom.live.mapping.fields;

import de.flapdoodle.mongoom.AbstractDatastoreTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.live.beans.fields.Book;

public class TestBook extends AbstractDatastoreTest {

//	private IDatastore _datastore;
	
	
	public TestBook() {
		super(Book.class);
	}

	public void testQuery() {
		IDatastore datastore = getDatastore();
		
		datastore.insert(Book.getInstance("Das Leben", "sommer", "sonne", "sachbuch"));
		datastore.insert(Book.getInstance("Die Arbeit", "sachbuch"));
		datastore.insert(Book.getInstance("Das Hobby", "sachbuch", "sonne"));
		datastore.insert(Book.getInstance("Dei Freizeit", "sommer"));

		assertEquals("sommer", 2, datastore.with(Book.class).listfield(Book.Category).eq("sommer").result().countAll());

		Book book = datastore.with(Book.class).field(Book.Name).eq("Das Leben").result().get();
		assertNotNull("Book", book);
		//		book.setCategory(Lists.newArrayList("blau","wal"));
		datastore.update(book);
	}

//	@Override
//	protected void setUp() throws Exception {
//		super.setUp();
//
//		ObjectMapper mongoom = new ObjectMapper();
//		mongoom.map(Book.class);
//
//		IDatastore datastore = mongoom.createDatastore(getMongo(), getDatabaseName());
//
//		datastore.ensureCaps();
//		datastore.ensureIndexes();
//
//		_datastore = datastore;
//	}

//	@Override
//	protected boolean cleanUpAfterTest() {
//		return false;
//	}
}
