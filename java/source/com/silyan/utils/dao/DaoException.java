/**
 * 
 */
package com.silyan.utils.dao;

import javax.ejb.ApplicationException;

/**
 * @author angel
 *
 */
@ApplicationException(rollback=true)
public class DaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3351509853679846349L;

	public DaoException() {
		super();
	}
	
	public DaoException(String message) {
		super(message);
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

}
