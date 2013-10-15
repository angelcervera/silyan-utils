/**
 * 
 */
package com.silyan.helpers.dao.dto;

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
