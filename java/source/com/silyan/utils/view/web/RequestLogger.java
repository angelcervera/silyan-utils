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
package com.silyan.utils.view.web;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Angel Cervera Claudio (angelcervera@silyan.com)
 *
 */
public class RequestLogger implements Filter {


	private String LOGNAME = RequestLogger.class.getName();

    /**
     * Default constructor.
     */
    public RequestLogger() {
    }

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(Logger.getLogger(LOGNAME).isLoggable(Level.FINE)) {
			if (request instanceof HttpServletRequest) {
				Logger.getLogger(LOGNAME).fine( infoRequest( (HttpServletRequest) request).toString() );
			} else {
				Logger.getLogger(LOGNAME).fine("Petición no es HttpServletRequest");
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * Devuelve un información sobre el request.
	 *
	 * @param request
	 * @return
	 */
	public static StringBuffer infoRequest(HttpServletRequest request) {

		StringBuffer retValue = new StringBuffer();

		retValue.append("request.getAuthType(): " + request.getAuthType() + '\n');
		retValue.append("request.getCharacterEncoding(): " + request.getCharacterEncoding() + '\n');
		retValue.append("request.getContentLength(): " + request.getContentLength() + '\n');
		retValue.append("request.getContentType(): " + request.getContentType() + '\n');
		retValue.append("request.getContextPath(): " + request.getContextPath() + '\n');
		retValue.append("request.getLocalAddr(): " + request.getLocalAddr() + '\n');
		retValue.append("request.getLocalName(): " + request.getLocalName() + '\n');
		retValue.append("request.getLocalPort(): " + request.getLocalPort() + '\n');
		retValue.append("request.getMethod(): " + request.getMethod() + '\n');
		retValue.append("request.getPathInfo(): " + request.getPathInfo() + '\n');
		retValue.append("request.getPathTranslated(): " + request.getPathTranslated() + '\n');
		retValue.append("request.getProtocol(): " + request.getProtocol() + '\n');
		retValue.append("request.getQueryString(): " + request.getQueryString() + '\n');
		retValue.append("request.getRemoteAddr(): " + request.getRemoteAddr() + '\n');
		retValue.append("request.getRemoteHost(): " + request.getRemoteHost() + '\n');
		retValue.append("request.getRemotePort(): " + request.getRemotePort() + '\n');
		retValue.append("request.getRemoteUser(): " + request.getRemoteUser() + '\n');
		retValue.append("request.getRequestedSessionId(): " + request.getRequestedSessionId() + '\n');
		retValue.append("request.getRequestURI(): " + request.getRequestURI() + '\n');
		retValue.append("request.getScheme(): " + request.getScheme() + '\n');
		retValue.append("request.getServerName(): " + request.getServerName() + '\n');
		retValue.append("request.getServerPort(): " + request.getServerPort() + '\n');
		retValue.append("request.getServletPath(): " + request.getServletPath() + '\n');
		retValue.append("request.getLocale(): " + request.getLocale() + '\n');
		retValue.append("request.getRequestURL(): " + request.getRequestURL() + '\n');

		retValue.append("-------- PARAMETERS:\n");
		Enumeration<String> strParametersNames = request.getParameterNames();
		while (strParametersNames.hasMoreElements()) {
			String name = (String) strParametersNames.nextElement();
			String[] values = request.getParameterValues(name);
			retValue.append("[").append(name).append("]: [");
			if(values.length == 1) {
				retValue.append(values[0]);
			} else {
				retValue.append("\n");
				for (String value : values) {
					retValue.append("\t{").append(value).append("}\n");
				}
			}
			retValue.append("]\n");

		}

		retValue.append("-------- HEADERS:\n");
		Enumeration<String> strHeadersNames = request.getHeaderNames();
		while (strHeadersNames.hasMoreElements()) {
			String name = (String) strHeadersNames.nextElement();
			Enumeration<String> values = request.getHeaders(name);
			retValue.append("[").append(name).append("]: [");
			retValue.append("\n");
			while(values.hasMoreElements()) {
				retValue.append("\t{").append(values.nextElement()).append("}\n");
			}
			retValue.append("Date: ");
			try {
				long date = request.getDateHeader(name);
				if(date >= 0) {
					retValue.append(DateFormat.getInstance().format(new Date(date))).append('[').append(date).append(']');
				} else {
					retValue.append("UNAVAILABLE");
				}
			} catch(java.lang.IllegalArgumentException e) {
				retValue.append("UNAVAILABLE");
			}

			retValue.append("]\n");

		}

		retValue.append("-------- ATRIBUTES:\n");
		Enumeration<String> strAttributesNames = request.getAttributeNames();
		while (strAttributesNames.hasMoreElements()) {
			String name = (String) strAttributesNames.nextElement();
			retValue.append("[").append(name).append("]: [").append(request.getAttribute(name)).append('\n');

		}

		retValue.append("-------- LOCALES:\n");
		Locale defaultLocale = request.getLocale();
		Enumeration<Locale> strLocales = request.getLocales();
		while (strLocales.hasMoreElements()) {
			Locale locale = (Locale) strLocales.nextElement();
			retValue.append('\t').append(locale);
			if(locale.equals(defaultLocale)) {
				retValue.append(" - DEFAULT");
			}
			retValue.append('\n');
		}

		return retValue;
	}


}
