package com.silyan.utils.generic;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.silyan.utils.generic.DateUtils;

public class DateUtilsTest {
	
	SimpleDateFormat sdf;
	
	@BeforeClass
	public void init() {
		 sdf = new SimpleDateFormat("yyyy-MM-dd");
		 sdf.setLenient(false);
	}
	
	@Test
	public void daysDiff() throws ParseException {
		Assert.assertEquals( DateUtils.daysDiff(sdf.parse("2012-12-31"), sdf.parse("2013-1-1")), 1L);
		Assert.assertEquals( DateUtils.daysDiff(sdf.parse("2013-2-1"), sdf.parse("2013-2-28")), 27L);
		Assert.assertEquals( DateUtils.daysDiff(sdf.parse("2013-2-1"), sdf.parse("2013-3-1")), 28L);
		Assert.assertEquals( DateUtils.daysDiff(sdf.parse("2016-2-27"), sdf.parse("2016-2-29")), 2L); // bisiesto
		Assert.assertEquals( DateUtils.daysDiff(sdf.parse("2016-2-27"), sdf.parse("2016-3-1")), 3L); // bisiesto
		Assert.assertEquals( DateUtils.daysDiff(sdf.parse("2013-2-27"), sdf.parse("2013-3-1")), 2L); // no bisiesto
		Assert.assertEquals( DateUtils.daysDiff(sdf.parse("2016-1-1"), sdf.parse("2017-1-1")), 366L); // bisiesto
		Assert.assertEquals( DateUtils.daysDiff(sdf.parse("2013-1-1"), sdf.parse("2014-1-1")), 365L); // no bisiesto
		Assert.assertEquals( DateUtils.daysDiff(sdf.parse("2013-3-30"), sdf.parse("2013-4-3")), 3L); // spanish change hour.
		Assert.assertEquals( DateUtils.daysDiff(sdf.parse("2013-10-26"), sdf.parse("2013-10-30")), 4L); // spanish change hour.
	}
}
