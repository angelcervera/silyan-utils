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
