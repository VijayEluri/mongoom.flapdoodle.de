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

package de.flapdoodle.mongoom.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogConfig {

	public static Logger getLogger(Class<?> clazz) {
		return Logger.getLogger(clazz.getName());
	}
	
	public static void setLevel(Logger logger, Level newLevel) {
		logger.setLevel(newLevel);
		setHandlerLevel(logger, newLevel);
		Logger current=logger;
		do {
			current=current.getParent();
			if (current!=null) {
				setHandlerLevel(current, newLevel);
			}
		}
		while (current!=null);
	}

	private static void setHandlerLevel(Logger logger, Level newLevel) {
		for (Handler h : logger.getHandlers()) {
			h.setLevel(newLevel);
		}
	}

}
