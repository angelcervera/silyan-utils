/**
 * 
 */
package com.silyan.utils.dao;

import com.silyan.utils.dao.dto.SearchWrapper;

/**
 * @author angelcervera@silyan.com
 *
 */
public interface SearchDAO<T> {
	
	/**
	 * Execute a search.
	 * 
	 * @param searchWrapper Search definition.
	 * @throws Exception No rollback necessary in search, so exception doesn't mark transaction to roollback.
	 */
	public void search(SearchWrapper<T> searchWrapper) throws Exception;

}
