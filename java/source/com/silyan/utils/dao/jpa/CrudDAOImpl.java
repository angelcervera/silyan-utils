/*
 * Copyright 2013 - Ángel Cervera Claudio
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
