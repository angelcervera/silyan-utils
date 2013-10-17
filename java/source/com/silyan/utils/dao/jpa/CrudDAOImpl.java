/**
 * 
 */
package com.silyan.utils.dao.jpa;

import java.util.Collection;
import java.util.List;

import com.silyan.utils.dao.CrudDAO;
import com.silyan.utils.dao.DaoException;
import com.silyan.utils.dao.dto.SearchOrder;
import com.silyan.utils.dao.dto.SearchWrapper;

/**
 * @author angel
 *
 */
public abstract class CrudDAOImpl<T,PK> extends SearchDAOImpl<T> implements CrudDAO<T, PK> {

	@Override
	public T insert(T e) throws DaoException {
		getEntityManager().persist(e);
		return e;
	}

	@Override
	public T update(T e) throws DaoException {
		getEntityManager().merge(e);
		return e;
	}

	@Override
	public void delete(T e) throws DaoException {
		getEntityManager().remove(getEntityManager().merge(e));
	}

	@Override
	public void deleteByPrimaryKey(PK id) throws DaoException {
		delete(retrieveByPrimaryKey(id));
	}

	@Override
	public T retrieve(T e) throws DaoException {
		getEntityManager().refresh(e);
		return e;
	}

	@Override
	public T retrieveByPrimaryKey(PK id) throws DaoException {
		return retrieveByPrimaryKey(id, null);
	}

	@Override
	public T retrieveByPrimaryKey(PK id, Collection<String> blocks) throws DaoException {
		return getEntityManager().find(getType(), id);
	}

	@Override
	public List<T> retrieveAll(List<SearchOrder> order, Collection<String> blocks) throws Exception {
		SearchWrapper<T> searchWrapper = new SearchWrapper<>();
		searchWrapper.setOrderBy(order);
		searchWrapper.setBlocks(blocks);
		searchWrapper.setPage(null);
		search(searchWrapper);
		return searchWrapper.getResult();
	}

	@Override
	public List<T> retrieveAll() throws Exception {
		return retrieveAll(null, null);
	}

}
