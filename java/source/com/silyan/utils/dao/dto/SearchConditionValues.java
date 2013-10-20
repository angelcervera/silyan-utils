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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Valores que se aplicarán sobre una condición (SearchCondition)
 * 
 * @author Angel Cervera Claudio (angelcervera@silyan.com)
 *
 */
public class SearchConditionValues implements Serializable {
	
	// Guardamos los valores en orden de inserción.
	private List<Object> conditionsList = new ArrayList<Object>();
	
	// Guardamos los valores indexados por el nombre del campo.
	private Map<String, Object> conditionsMap = new HashMap<String, Object>();

	/**
	 * 
	 */
	private static final long serialVersionUID = 2396912711697877111L;

	/**
	 * Condición de la cual indica los valores.
	 */
	private SearchCondition searchCondition;
	
	public SearchConditionValues() {
	}

	/**
	 * Crea el objeto con los valores de la condición, relacionándola con la condición.
	 * 
	 * @param searchCondition
	 */
	public SearchConditionValues(SearchCondition searchCondition) {
		this.searchCondition = searchCondition;
	}

	/**
	 * Crea el objeto con los valores de la condición, relacionándola con la condición.
	 * Además, asocia un valor al primer elemento de la condición.
	 * 
	 * @param searchCondition
	 * @param value
	 */
	public SearchConditionValues(SearchCondition searchCondition, Object value) {
		this.searchCondition = searchCondition;
		conditionsMap.put(searchCondition.getFieldName()[0], value);
		conditionsList.add(value);
	}

	/**
	 * @return the searchCondition
	 */
	public SearchCondition getSearchCondition() {
		return searchCondition;
	}

	/**
	 * @param searchCondition the searchCondition to set
	 */
	public void setSearchCondition(SearchCondition searchCondition) {
		this.searchCondition = searchCondition;
	}
	
	/**
	 * Añade un valor para un determinado parámetro.
	 * 
	 * @param parameterName
	 * @param value
	 */
	public void addValue(String parameterName, Object value) {
		conditionsMap.put(parameterName, value);
		conditionsList.add(value);
	}
	
	/**
	 * Añade un valor para el único parámetro que hay.
	 * 
	 * @param parameterName
	 * @param value
	 */
	public void addValue(Object value) {
		conditionsMap.put(searchCondition.getFieldName()[0], value);
		conditionsList.add(value);
	}

	/**
	 * @return the conditionsList
	 */
	public List<Object> getConditionsList() {
		return conditionsList;
	}

	/**
	 * @return the conditionsMap
	 */
	public Map<String, Object> getConditionsMap() {
		return conditionsMap;
	}
	
}
