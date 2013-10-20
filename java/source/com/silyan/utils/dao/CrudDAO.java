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
