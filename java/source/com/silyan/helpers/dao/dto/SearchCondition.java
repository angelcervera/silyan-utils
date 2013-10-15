/**
 * 
 */
package com.silyan.helpers.dao.dto;

import java.io.Serializable;

/**
 * Representación de una condición de búsqueda.
 * 
 * @author Angel Cervera Claudio
 */
public class SearchCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5837716179818348215L;
	
	/**
	 * Nombre de los campos que intervienen como parámetros en la búsqueda.
	 */
	protected String[] fieldName = null;
	
	
	/**
	 * Tipo de dato de los campos que intervienen como parámetros en la búsqueda.
	 */
	protected String[] fieldDeclaration = null;
	
	/**
	 * Condición en la búsqueda, es decir, parte de lo que será el where.
	 */
	protected String filterCondition = "";
	
	/**
	 * Unión con la parte anterior de la búsqueda.
	 */
	protected String logicUnion = "";
	
	public SearchCondition() {
	}
	
	/**
	 * fieldName y fieldDeclaration puede contener una lista de elementos, separados por coma.
	 * 
	 * @param fieldName  Nombre de los campos que intervienen como parámetros en la búsqueda, separados con comas.
	 * @param fieldDeclaration Tipo de dato de los campos que intervienen como parámetros en la búsqueda, separados por comas.
	 * @param filterCondition Condición en la búsqueda, es decir, parte de lo que será el where.
	 * @param logicUnion Unión con la parte anterior de la búsqueda.
	 */
	public SearchCondition(String fieldName, String fieldDeclaration, String filterCondition, String logicUnion) {
		if(fieldName != null) this.fieldName = fieldName.split(",");
		if(fieldDeclaration != null) this.fieldDeclaration = fieldDeclaration.split(",");
		this.filterCondition = filterCondition;
		this.logicUnion = logicUnion;
	}
	
	/**
	 * La condifidición creada mediante este constructor difiere en que la condición no admitirá variables.
	 * Es decir, crea un filtro constante.
	 * 
	 * @param fieldName  Nombre de los campos que intervienen como parámetros en la búsqueda, separados con comas.
	 * @param fieldDeclaration Tipo de dato de los campos que intervienen como parámetros en la búsqueda, separados por comas.
	 * @param filterCondition Condición en la búsqueda, es decir, parte de lo que será el where.
	 * @param logicUnion Unión con la parte anterior de la búsqueda.
	 */
	public SearchCondition( String filterCondition, String logicUnion) {
		this.filterCondition = filterCondition;
		this.logicUnion = logicUnion;
	}

	/**
	 * @return Returns the fieldDeclaration.
	 */
	public String[] getFieldDeclaration() {
		return fieldDeclaration;
	}

	/**
	 * @return Returns the fieldName.
	 */
	public String[] getFieldName() {
		return fieldName;
	}

	/**
	 * @return Returns the filterCondition.
	 */
	public String getFilterCondition() {
		return filterCondition;
	}

	/**
	 * @return Returns the logicUnion.
	 */
	public String getLogicUnion() {
		return logicUnion;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String[] fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @param fieldDeclaration the fieldDeclaration to set
	 */
	public void setFieldDeclaration(String[] fieldDeclaration) {
		this.fieldDeclaration = fieldDeclaration;
	}

	/**
	 * @param filterCondition the filterCondition to set
	 */
	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}

	/**
	 * @param logicUnion the logicUnion to set
	 */
	public void setLogicUnion(String logicUnion) {
		this.logicUnion = logicUnion;
	}
	
}
