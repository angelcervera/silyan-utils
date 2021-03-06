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

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.silyan.utils.dao.SearchDAO;
import com.silyan.utils.dao.dto.SearchCondition;
import com.silyan.utils.dao.dto.SearchConditionValues;
import com.silyan.utils.dao.dto.SearchOrder;
import com.silyan.utils.dao.dto.SearchWrapper;

/**
 * @author Angel Cervera Claudio (angelcervera@silyan.com)
 *
 */
public abstract class SearchDAOImpl<T> implements SearchDAO<T> {
	
	private String LOGNAME = SearchDAOImpl.class.getName();
	
	protected abstract Class<T> getType();
	
	/**
	 * @return the entityManager
	 */
	public abstract EntityManager getEntityManager();
	
	/**
	 * Execute a search.
	 * 
	 * @param searchWrapper Search definition.
	 * @throws Exception No rollback necessary in search, so exception doesn't mark transaction to roollback.
	 */
	@Override
	public void search(SearchWrapper<T> searchWrapper) throws Exception {
		
		long initmm = System.currentTimeMillis();
		
		//	Contruct of filter.
		String strQueryFilter = createQueryFilter(searchWrapper);
		if(Logger.getLogger(LOGNAME).isLoggable(Level.FINE)) {
			Logger.getLogger(LOGNAME).fine("strQueryFilter: " + strQueryFilter);
		}
		
		//	Set order.
		StringBuffer orderStb = new StringBuffer();
		List<SearchOrder> lstOrder = searchWrapper.getOrderBy();
		String strOrder = null;
		if(lstOrder != null && lstOrder.size() > 0) {
                    boolean fFirst = true;
                    for (SearchOrder searchOrder : searchWrapper.getOrderBy()) {
                        if(fFirst) {
                            fFirst = false;
                            orderStb.append(' ');
                        } else {
                            orderStb.append(" , ");
                        }
                        
                        
                        if(searchOrder.isAscending()) {
                        	strOrder = "asc";
                        } else {
                        	strOrder = "desc";
                        }
                        orderStb.append(searchOrder.getField()).append(' ').append(strOrder);
                    }
                    if(Logger.getLogger(LOGNAME).isLoggable(Level.FINE)) {
                    	Logger.getLogger(LOGNAME).fine("Order: " + orderStb.toString());
                    }
		}
		
		String strQueryOrder = orderStb.toString();
		
		if(Logger.getLogger(LOGNAME).isLoggable(Level.FINE)) {
			Logger.getLogger(LOGNAME).fine("Filter: " + strQueryFilter);
			Logger.getLogger(LOGNAME).fine("Order: " + strQueryOrder);
		}	
		
		//	Generate and Execute Query.
		String strQuery;
		if( searchWrapper.getCustomQuery() == null || searchWrapper.getCustomQuery().isEmpty() ) {
			strQuery = "select this from " + getType().getSimpleName() + " this "+ createDynamicStringQuery(strQueryFilter, strQueryOrder).toString();
		} else {
			String queryWhere = "";
			if(strQueryFilter != null && strQueryFilter.length() > 0) {
				queryWhere = " where " + strQueryFilter;
			}
			
			String queryOrderBy = "";
			if(strQueryOrder != null && strQueryOrder.length() > 0) {
				queryOrderBy = " order by " + strQueryOrder;
			}
			
			strQuery = String.format( searchWrapper.getCustomQuery(), queryWhere, queryOrderBy );
		}
		if(Logger.getLogger(LOGNAME).isLoggable(Level.FINE)) {
			Logger.getLogger(LOGNAME).fine("Query: " + strQuery);
		}
		
		
		Query q;
		if(searchWrapper.isNativeQuery()) {
			q = getEntityManager().createNativeQuery(strQuery);
		} else {
			q = getEntityManager().createQuery(strQuery);
		}
		
		//	Set Range, if exist.
		if(searchWrapper.getPage() != null && searchWrapper.getPageSize() != null ) {
			int maxResult = searchWrapper.getPageSize().intValue();
			int firstResult = searchWrapper.getPage().intValue() * searchWrapper.getPageSize().intValue();
			int fetchSize = searchWrapper.getPageSize().intValue();
			q.setMaxResults(maxResult);
			q.setFirstResult(firstResult);
//			q.setFetchSize(fetchSize); // TODO: setFetchSize Cómo en JPA??
			q.setHint("eclipselink.jdbc.fetch-size", fetchSize);
			q.setHint("openjpa.FetchBatchSize", fetchSize);
			if(Logger.getLogger(LOGNAME).isLoggable(Level.FINE)) {
				Logger.getLogger(LOGNAME).fine("maxResult: " + maxResult);
				Logger.getLogger(LOGNAME).fine("firstResult: " + firstResult);
				Logger.getLogger(LOGNAME).fine("fetchSize: " + fetchSize);
			}
		}
		
		setParameters(searchWrapper, q);
		
		// Ejecutamos la consulta.
		searchWrapper.setResult((List<T>)q.getResultList());
		
		if(searchWrapper.getObtainResultSize()) {
			
			String countQuery;
			if(searchWrapper.getCustomQuery() == null || searchWrapper.getCustomQuery().isEmpty()) {
				countQuery = "select count(this) from " + getType().getSimpleName() + " this ";
				if(strQueryFilter != null && strQueryFilter.length() > 0) {
					countQuery = countQuery + " where " + strQueryFilter;
				}
			} else {
				String queryWhere = "";
				if(strQueryFilter != null && strQueryFilter.length() > 0) {
					queryWhere = "where " + strQueryFilter;
				}
				countQuery = String.format( searchWrapper.getCustomCountQuery(), queryWhere);
			}
			
			if(Logger.getLogger(LOGNAME).isLoggable(Level.FINE)) {
				Logger.getLogger(LOGNAME).fine("Count Query: " + countQuery);
			}
			
			Query qcount;
			if(searchWrapper.isNativeQuery()) {
				qcount = getEntityManager().createNativeQuery(countQuery);
			} else {
				qcount = getEntityManager().createQuery(countQuery);
			}
			
			setParameters(searchWrapper, qcount);
			
			searchWrapper.setResultSize( new Integer(((Long) qcount.getSingleResult()).intValue()));
			
			// Repositioning in last pages, if selected pages is greater than last page.
			if( !searchWrapper.getResultSize().equals(0) && searchWrapper.getPage() != null && searchWrapper.getPage() >= searchWrapper.getPagesCount() ) {
				searchWrapper.setPage( searchWrapper.getPagesCount() -1);
				search(searchWrapper);
			}
		}
		
		if(Logger.getLogger(LOGNAME).isLoggable(Level.FINE)) {
			Logger.getLogger(LOGNAME).fine("Elementos encontrados: [" + searchWrapper.getResultSize() + "] Tiempo: " +  (System.currentTimeMillis() - initmm) + "ms. ( count(*): "+searchWrapper.getObtainResultSize()+")");
		}

	}

	/**
	 * Genera la parte del where de la búsqueda.
	 * 
	 * @param searchWrapper
	 * @return
	 */
	protected String createQueryFilter(SearchWrapper<T> searchWrapper) {
		StringBuffer filter = new StringBuffer();		
		
		for (Map.Entry<String, SearchConditionValues> entry : searchWrapper.getSearchValues().entrySet()) {
			Object key = entry.getKey();
			
			SearchCondition searchCondition = searchWrapper.getSearchFiltersMap().get(key);
			
			if(searchCondition !=null ) {
				if(filter.length() > 0) {
					filter.append(" ").append(searchCondition.getLogicUnion()).append(" ");
				}				
				
			    filter.append(searchCondition.getFilterCondition());

			} else {
				Logger.getLogger(LOGNAME).info("No se ha encontrado condición con la clave " + key);
			}
		}

		String strQueryFilter = filter.toString();
		return strQueryFilter;
	}

	/**
	 * Establece los valores del where.
	 * 
	 * @param searchWrapper
	 * @param q
	 * @throws HibernateException
	 */
	private void setParameters(SearchWrapper<T> searchWrapper, Query q) {
		for (Map.Entry<String, SearchConditionValues> entry : searchWrapper.getSearchValues().entrySet()) {
			SearchConditionValues searchConditionValues = entry.getValue();
			Map<String, Object> mapValues = searchConditionValues.getConditionsMap();
			String[] fieldName = searchConditionValues.getSearchCondition().getFieldName();
			if( fieldName != null && fieldName.length > 0) {
				for (int idx = 0; idx < fieldName.length; idx++) {
					q.setParameter(fieldName[idx], mapValues.get(fieldName[idx]));
				}
			}
		}
	}
	
	/**
	 * Crea la parte dinámica de una query.
	 * @param where
	 * @param orderby
	 * @return
	 */
	private StringBuffer createDynamicStringQuery(String where, String orderby) {
		StringBuffer retValue = new StringBuffer();
		if(where != null && where.length() > 0) {
			retValue.append(" where ").append(where);
		}
		if(orderby != null && orderby.length() > 0) {
			retValue.append(" order by ").append(orderby);
		}
		return retValue;
	}

}
