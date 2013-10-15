/**
 * 
 */
package com.silyan.helpers.dao.dto;

import java.io.Serializable;

/**
 * Representación de un orden de los resultados de la búsqueda.
 * 
 * @author Angel Cervera Claudio
 *
 */
public class SearchOrder  implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3702175152329602019L;
	
	public String field;

	public boolean ascending;
	
	public SearchOrder() {
	}
	
	public SearchOrder(String field, boolean ascending) {
		this.field = field;
		this.ascending = ascending;
	}
	
	public SearchOrder(String field, Boolean ascending) {
		this.field = field;
		if(ascending==null) ascending = new Boolean(null);
		this.ascending = ascending.booleanValue();
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return the ascending
	 */
	public boolean isAscending() {
		return ascending;
	}

	/**
	 * @param ascending the ascending to set
	 */
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
	
}
