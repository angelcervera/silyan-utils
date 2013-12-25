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
package com.silyan.utils.dao.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Objecto que contendrá la definición de una búsqueda, así como el resultado de la misma.
 * 
 * @author Angel Cervera Claudio
 *
 */
public class SearchWrapper<E> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2343307334576631555L;

	/**
	 * Página que queremos obtener.
	 * La primera, la "0".
	 */
	private Integer page = new Integer(0);
	
	/**
	 * Tamaño de la página.
	 */
	private Integer pageSize = null;
	
	/**
	 * Resultado de la búsqueda.
	 */
	private List<E> result = new ArrayList<E>();
	
	/**
	 * Número de elementos encontrados en la búsqueda. Sólo si obtainResultSize = true.
	 */
	private Integer resultSize = new Integer(0);
	
	/**
	 * Indica si se desea obetener el número de registros.
	 */
	private boolean obtainResultSize = true;
	
	/**
	 * Conjunto de condiciones que definen una búsqueda.
	 * Cada condición representa un fragmento de la búsqueda que se usará o no, según se indique en searchValues.
	 */
	private Map<String, SearchCondition> searchFiltersMap = new HashMap<String, SearchCondition>();
	
	/**
	 * Valores con los datos por los que se va a buscar.
	 * La clave deberá coincidir con un elemento en searchFilterMap.
	 * Como valor, contendrá otro HashMap con los valores de los campo que intervienen en esa condición de búsqueda.
	 */
	private Map<String, SearchConditionValues> searchValues = new HashMap<String, SearchConditionValues>();
	
	private List<SearchOrder> orderBy = new ArrayList<SearchOrder>();
	
	private Collection<String> blocks; // Soportado por JPA?
	
	private Integer fetchDepth; // Soportado por JPA?

    /**
	 * Patrón usado para realizar la query, en formato indicado en http://java.sun.com/j2se/1.5.0/docs/api/java/util/Formatter.html
	 * {1} -> lugar donde se incrustará el where generado.
	 * {2} -> lugar donde se incrustará el orderby generado.
	 */
    private String customQuery = null;


	/**
	 * Patrón usado para realizar la query que cuenta el número de registros obtenidos, en formato indicado en http://java.sun.com/j2se/1.5.0/docs/api/java/util/Formatter.html
	 * La query deberá devolver un sólo registro.
	 * {1} -> lugar donde se incrustará el where generado.
	 */
	private String customCountQuery = null;

    /**
	 * Indica si es una query nativa o utiliza el lenguaje propio de la implementación.
	 */
    private boolean nativeQuery = false;


	public SearchWrapper() {
	}
	
	public SearchWrapper(Map<String, SearchCondition> searchFiltersMap) {
		this.searchFiltersMap = searchFiltersMap;
	}

	public SearchWrapper(Integer page, Integer pageSize, boolean obtainResultSize) {
		setPage(page);
		setPageSize(pageSize);
		setObtainResultSize(obtainResultSize);
	}

	/**
	 * Algunas de estas funcionalidades no se podrán implementar según el framework de persistencia que se use.
	 * Por ejemplo, en hibernate no existe el concepto de "bloques de obtención".
	 * 
	 * @param page Página que se desea obtener
	 * @param pageSize Tamaño de cada página
	 * @param obtainResultSize true si queremos que calcule el número de registros completo.
	 * @param searchFieldsMap Todos los filtro que se pueden usar en la búsqueda.
	 * @param searchValues Valores para los fitros que se quieren usar en la búsqueda.
	 * @param orderBy Orden
	 * @param orderByDirection Oscendente o Descentente
	 * @param blocks Bloques (conjunto de campos) que se desean obtener en la consulta.
	 */
	public SearchWrapper(Integer page, Integer pageSize, boolean obtainResultSize, Map <String, SearchCondition> searchFiltersMap, Map<String, SearchConditionValues> searchValues, List<SearchOrder> orderBy, Boolean orderByDirection, List<String> blocks) {
		if(page==null) setPage(new Integer(0)); else setPage(page);
		setPageSize(pageSize);
		setObtainResultSize(obtainResultSize);
		setSearchFiltersMap(searchFiltersMap);
		setSearchValues(searchValues);
		setOrderBy(orderBy);
//		setOrderDirectionBy(orderByDirection);
		setBlocks(blocks);
	}

	/**
	 * @return Returns the blocks.
	 */
	public Collection<String> getBlocks() {
		return blocks;
	}

	/**
	 * @param blocks The blocks to set.
	 */
	public void setBlocks(Collection<String> blocks) {
		this.blocks = blocks;
	}

	/**
	 * @return the orderBy
	 */
	public List<SearchOrder> getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(List<SearchOrder> orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * Number of pages.
	 * 
	 * @return
	 */
	public int getPagesCount() {
		double count = (double)resultSize / (double)pageSize;
		double pageCount = Math.floor(count);
		if(pageCount < count) {
			pageCount++;
		}
		return (int)pageCount;
	}
	
	/**
	 * @return Returns the page.
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * La primera, la "0".
	 * @param page The page to set.
	 */
	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * @return Returns the pageSize.
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize The pageSize to set.
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return Returns the result.
	 */
	public List<E> getResult() {
		return result;
	}

	/**
	 * @param result The result to set.
	 */
	public void setResult(List<E> result) {
		this.result = result;
	}

	/**
	 * @return the searchFiltersMap
	 */
	public Map<String, SearchCondition> getSearchFiltersMap() {
		return searchFiltersMap;
	}

	/**
	 * @param searchFiltersMap the searchFiltersMap to set
	 */
	public void setSearchFiltersMap(Map<String, SearchCondition> searchFiltersMap) {
		this.searchFiltersMap = searchFiltersMap;
	}

	/**
	 * @return the searchValues
	 */
	public Map<String, SearchConditionValues> getSearchValues() {
		return searchValues;
	}

	/**
	 * @param searchValues the searchValues to set
	 */
	public void setSearchValues(Map<String, SearchConditionValues> searchValues) {
		this.searchValues = searchValues;
	}

	/**
	 * @return Returns the isResultSize.
	 */
	public boolean getObtainResultSize() {
		return obtainResultSize;
	}

	/**
	 * @param isResultSize The isResultSize to set.
	 */
	public void setObtainResultSize(boolean obtainResultSize) {
		this.obtainResultSize = obtainResultSize;
	}

	/**
	 * @return the resultSize
	 */
	public Integer getResultSize() {
		return resultSize;
	}

	/**
	 * @param resultSize the resultSize to set
	 */
	public void setResultSize(Integer resultSize) {
		this.resultSize = resultSize;
	}
	
	/**
	 * @return the fetchDepth
	 */
	public Integer getFetchDepth() {
		return fetchDepth;
	}

	/**
	 * @param fetchDepth the fetchDepth to set
	 */
	public void setFetchDepth(Integer fetchDepth) {
		this.fetchDepth = fetchDepth;
	}
	
	/**
	 * Añade las condiciones que se le pasan como parámetros.
	 * @param conditionName
	 * @return
	 */
	public void addConditionValues(Map<String, Object> conditions) {
		for (String conditionName : conditions.keySet()) {
			Object value = conditions.get(conditionName);
			addConditionValues(conditionName, value);
		}
	}

	/**
	 * Recupera el objeto que contiene los valores para un determinado filtro ( una condición ).
	 * 
	 * @param conditionName
	 * @return
	 */
	public SearchConditionValues addConditionValues(String conditionName) {
		SearchConditionValues searchConditionValues = getSearchValues().get(conditionName);
		if(searchConditionValues == null) {
			SearchCondition searchCondition = getSearchFiltersMap().get(conditionName);
			if(searchCondition == null) {
				throw new RuntimeException("Condition " +conditionName+ " do not defined");
			}
			searchConditionValues = new SearchConditionValues(searchCondition);
			getSearchValues().put(conditionName, searchConditionValues);
		}
		return searchConditionValues;
	}
	
	/**
	 * Recupera el objeto que contiene los valores para un determinado filtro ( una condición ).
	 * Además, añade el valor que se pasa como parámetro a la condición.
	 * 
	 * @param conditionName
	 * @return
	 */
	public SearchConditionValues addConditionValues(String conditionName, Object value) {
		SearchConditionValues searchConditionValues = addConditionValues(conditionName);
		if(value != null) {
			searchConditionValues.addValue(value);
		}
		return searchConditionValues;
	}

    public String getCustomCountQuery() {
        return customCountQuery;
    }

    public void setCustomCountQuery(String customCountQuery) {
        this.customCountQuery = customCountQuery;
    }

    public String getCustomQuery() {
        return customQuery;
    }

    public void setCustomQuery(String customQuery) {
        this.customQuery = customQuery;
    }

    public boolean isNativeQuery() {
        return nativeQuery;
    }

    public void setNativeQuery(boolean nativeQuery) {
        this.nativeQuery = nativeQuery;
    }
	
}
