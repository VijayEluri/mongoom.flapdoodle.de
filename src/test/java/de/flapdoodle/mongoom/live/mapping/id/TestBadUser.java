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

package de.flapdoodle.mongoom.live.mapping.id;

import de.flapdoodle.mongoom.AbstractDatastoreTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.exceptions.ObjectMapperException;
import de.flapdoodle.mongoom.exceptions.UpdateFailedException;
import de.flapdoodle.mongoom.live.beans.id.BadUser;

public class TestBadUser extends AbstractDatastoreTest {

//	private IDatastore _datastore;
	
	public TestBadUser() {
		super(BadUser.class);
	}

	public void testInsert() {
		IDatastore datastore = getDatastore();
		
		datastore.insert(BadUser.get("klaus", "Klaus"));
		datastore.insert(BadUser.get("peter", "Peter"));

		boolean exception = false;
		try {
			datastore.insert(BadUser.get("klaus", "Klaus2"));
		} catch (ObjectMapperException mx) {
			exception = true;
		}

		assertTrue("Exception", exception);
	}

	public void testVersion() {
		IDatastore datastore = getDatastore();
		
		datastore.insert(BadUser.get("klaus", "Klaus"));
		BadUser klaus = datastore.with(BadUser.class).field(BadUser.Name).eq("Klaus").result().get();
		BadUser klaus2 = datastore.with(BadUser.class).field(BadUser.Name).eq("Klaus").result().get();

		datastore.update(klaus);

		boolean updateFailed = false;
		try {
			datastore.update(klaus2);
		} catch (UpdateFailedException ux) {
			updateFailed = true;
		}

		assertTrue("Update Failed", updateFailed);
	}

//	@Override
//	protected void setUp() throws Exception {
//		super.setUp();
//
//		ObjectMapper mongoom = new ObjectMapper();
//		mongoom.map(BadUser.class);
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
