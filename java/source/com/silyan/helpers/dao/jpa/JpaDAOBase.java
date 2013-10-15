/**
 * 
 */
package com.silyan.helpers.dao.jpa;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.silyan.helpers.dao.dto.SearchCondition;
import com.silyan.helpers.dao.dto.SearchConditionValues;
import com.silyan.helpers.dao.dto.SearchOrder;
import com.silyan.helpers.dao.dto.SearchWrapper;

/**
 * @author Angel Cervera Claudio (angelcervera@silyan.com)
 *
 */
public abstract class JpaDAOBase<E> {
	
	protected abstract Class<E> getType();
	
	private String LOGNAME = JpaDAOBase.class.getName();
	
	/**
	 * Execute a search.
	 * 
	 * @param searchWrapper Search definition.
	 * @throws Exception No rollback necessary in search, so exception doesn't mark transaction to roollback.
	 */
	protected void search(SearchWrapper<E> searchWrapper) throws Exception {
		
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
		searchWrapper.setResult((List<E>)q.getResultList());
		
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
	protected String createQueryFilter(SearchWrapper<E> searchWrapper) {
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
	private void setParameters(SearchWrapper<E> searchWrapper, Query q) {
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

	/**
	 * @return the entityManager
	 */
	public abstract EntityManager getEntityManager();
	
}
