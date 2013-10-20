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
package com.silyan.utils.generic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author Angel Cervera Claudio ( angelcervera@silyan.com )
 *
 */
public class DateUtils {
	
	/**
	 * Number of days between two dates.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static long daysDiff(Date from, Date to) {
		return Math.round( (to.getTime() - from.getTime()) / 86400000D ); // 1000 * 60 * 60 * 24
	}
	
	/**
	 * Return UTC system's date.
	 * 
	 * @return
	 */
	public static Date getNow() {
		return GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
	}
	
	private final static String datePatter = "yyyy-MM-dd";
	private final static String dateTimePatter = "yyyy-MM-dd HH:mm:ss.SSS";
	private final static String timeZoneCode = "UTC";
	
	
	public static SimpleDateFormat buildSimpleDateFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat(datePatter);
		sdf.setTimeZone(TimeZone.getTimeZone(timeZoneCode));
		sdf.setLenient(false);
		return sdf;
	}
	
	public static SimpleDateFormat buildSimpleDateTimeFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimePatter);
		sdf.setTimeZone(TimeZone.getTimeZone(timeZoneCode));
		sdf.setLenient(false);
		return sdf;
	}
	
	public static Date parseDate(String strDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(datePatter);
		sdf.setTimeZone(TimeZone.getTimeZone(timeZoneCode));
		sdf.setLenient(false);
		return sdf.parse(strDate);
	}

	public static String formatDate(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(datePatter);
		sdf.setTimeZone(TimeZone.getTimeZone(timeZoneCode));
		sdf.setLenient(false);
		return sdf.format(date);
	}
	
	public static Date parseDateTime(String strDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimePatter);
		sdf.setTimeZone(TimeZone.getTimeZone(timeZoneCode));
		sdf.setLenient(false);
		return sdf.parse(strDate);
	}

	public static String formatDateTime(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimePatter);
		sdf.setTimeZone(TimeZone.getTimeZone(timeZoneCode));
		sdf.setLenient(false);
		return sdf.format(date);
	}

}
