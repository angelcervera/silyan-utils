/**
 * 
 */
package com.silyan.utils.dao;

import java.util.Collection;

import com.silyan.utils.dao.dto.SearchOrder;

/**
 * @author angel
 *
 */
public interface CrudDAO<T, PK> extends SearchDAO<T> {
	
	public T insert(T e) throws DaoException;

	public T update(T e) throws DaoException;

	public void delete(T e) throws DaoException;
	
	public void deleteByPrimaryKey(PK id) throws DaoException;

	public T retrieve(T e) throws DaoException;

	public T retrieveByPrimaryKey(PK id) throws DaoException;

	public T retrieveByPrimaryKey(PK id, java.util.Collection<String> blocks) throws DaoException;

	public PK retrievePrimaryKey(T e) throws DaoException;

	public java.util.List<T> retrieveAll(java.util.List<SearchOrder> order, Collection<String> blocks) throws Exception;

	public java.util.List<T> retrieveAll() throws Exception;

}
