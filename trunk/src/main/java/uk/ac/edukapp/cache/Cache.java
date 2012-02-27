/*
 *  (c) 2012 University of Bolton
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.edukapp.cache;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.jcs.engine.behavior.IElementAttributes;
import org.apache.log4j.Logger;

/**
 * Cache used for speeding up access to high-use objects - this a wrapper around Apache JCS
 * 
 * @author scottw
 *
 */
public class Cache {
	
	static Logger logger = Logger.getLogger(Cache.class.getName());	
	
	private static Cache instance;
	
	private JCS jcs;
	
	private Cache(){
		try {
			jcs = JCS.getInstance("edukapp");
		} catch (CacheException e) {
			logger.error("JCS error: cannot create cache", e);
		}
	}
	
	public static Cache getInstance(){
		if (instance == null) instance = new Cache();
		return instance;
	}
	
	/**
	 * Cache an object. The object is assumed to be "eternal" and will
	 * only be removed from cache to make room for more popular objects
	 * @param name the name for the cached object
	 * @param obj the object to cache
	 */
	public void put(String name, Object obj){
		try {
			jcs.put(name, obj);
		} catch (CacheException e) {
			logger.error("JCS error: cannot put object", e);
		}
	}
	
	/**
	 * Cache an object with a specified maximum lifetime in seconds
	 * @param name the name for the cached object
	 * @param obj the object to cache
	 * @param lifetime the maximum life in seconds for the cached object
	 */
	public void put(String name, Object obj, long lifetime) {
	    try {
			IElementAttributes attributes = jcs.getDefaultElementAttributes();
			attributes.setIsEternal(false);
			attributes.setMaxLifeSeconds(lifetime);
			jcs.put(name, obj, attributes);
		} catch (CacheException e) {
			logger.error("JCS error: cannot put object", e);
		}
	}
	
	/**
	 * Get the object from cache, if it exists
	 * @param name the name of the object
	 * @return the object, or null if it does not exist
	 */
	public Object get(Object name){
		return jcs.get(name);
	}

}
