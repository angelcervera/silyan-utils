package com.silyan.utils.generic;

import java.security.NoSuchAlgorithmException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.silyan.utils.generic.SecurityUtils;

public class SecurityUtilsTest {

	@Test
	public void sha1Hex() throws NoSuchAlgorithmException {
		Assert.assertEquals(SecurityUtils.sha1Hex("managermanager"), "5913bca037f5a5f75a69a28341163edc8c0a8b8b");
		Assert.assertEquals(SecurityUtils.sha1Hex("adminadmin"), "dd94709528bb1c83d08f3088d4043f4742891f4f");
		Assert.assertEquals(
				SecurityUtils.sha1Hex("11439044111950028000055405200000003221__0.6753030012614888330000000000019782120035304709122214340106007000"),
				"62753a72498191fd091750747b8750bc6cb06e8f"
			);
		Assert.assertEquals(
				SecurityUtils.sha1Hex(""),
				"da39a3ee5e6b4b0d3255bfef95601890afd80709"
			);
		Assert.assertEquals(
				SecurityUtils.sha1Hex("El coche amarillo"),
				"968be676ad7988e8d911fce686da3fececbb22eb"
			);
		Assert.assertEquals(
				SecurityUtils.sha1Hex("998888881119500280000554052000000031235009782SHA1http://www.ceca.eshttp://www.ceca.es"),
				"15ba153908476895d9edd75ff23b207707d2c885"
			);
	}

	@Test
	public void sha256SaltBase64() throws Exception {
		Assert.assertEquals(SecurityUtils.sha256SaltBase64("managermanager", "VGPDIYHC08"), "QvjgR3JT/u5Vp55BAY3TwiNhI+nGjLgMcLXWPtdKk74=");
		Assert.assertEquals(SecurityUtils.sha256SaltBase64("adminadmin", "MS8IYHTFRW"), "ddIP+nj0hKJIQJcPFB0zoqHopM3gAPRobvVxdf9+RxU=");
	}
	
	@DataProvider
	public Object[][] ValidPasswordProvider() {
		return new Object[][]{
				{new String[] {
						"mkyong1A@",
						"mkYOn12$",
						"123456x",
						"123456X",
						"mkyong12@",
						"mkyoNg12*",
						"MKYONG12$",
						"xxxxxx12$",
						"MMMMMM12$",
				}}
		};
	}

	@DataProvider
	public Object[][] InvalidPasswordProvider() {
		return new Object[][]{
				{new String[] {
						"mkyonG$$", // No numeric
						"mY1A@", // Very short
						"MKYONG12$12345678901234567890", // Very long
						"mOnxxxxx$", // No numeric
						"123456$", // No alphabetic
				}}
		};
	}
	
	@Test(dataProvider = "ValidPasswordProvider")
	public void checkValidPassword(String[] passwords) {
		for (String password : passwords) {
			Assert.assertTrue(SecurityUtils.checkPassword(password));
		}
	}
	
	@Test(dataProvider = "InvalidPasswordProvider")
	public void checkInvalidPassword(String[] passwords) {
		for (String password : passwords) {
			Assert.assertFalse(SecurityUtils.checkPassword(password));
		}
	}
}
