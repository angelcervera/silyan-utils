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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

/**
 * @author Angel Cervera Claudio ( angelcervera@silyan.com )
 *
 */
public class SecurityUtils {
	
	/**
	 * sha1 encryption utility.
	 * 
	 * @param message
	 * @return Hex. string value.
	 * @throws NoSuchAlgorithmException
	 */
	public static String sha1Hex(String message) throws NoSuchAlgorithmException {
    	byte[] buffer = message.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(buffer);
        byte[] digest = md.digest();

        // Manual method. DatatypeConverter is better ( I suppose )
//        String hash = "";
//        for(byte aux : digest) {
//            int b = aux & 0xff;
//            if (Integer.toHexString(b).length() == 1) hash += "0";
//            hash += Integer.toHexString(b);
//        }
        
        String hash = DatatypeConverter.printHexBinary(digest).toLowerCase();
        return hash;
	}
	
	/**
	 * 
	 * sha256 encryption with salt.
	 * 
	 * @param password
	 * @param salt
	 * @return Base64 string value
	 * @throws BusinessException
	 */
	public static String sha256SaltBase64(String password, String salt) throws Exception {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			byte[] binaryData = digest.digest( (salt + password).getBytes("UTF-8"));
			return DatatypeConverter.printBase64Binary(binaryData);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			throw new Exception("Error al encriptar el password.", e);
		}
	}
	
	/**
	 * Example regexp:
		(			# Start of group
		  (?=.*\d)		#   must contains one digit from 0-9
		  (?=.*[a-z])		#   must contains one lowercase characters
		  (?=.*[A-Z])		#   must contains one uppercase characters
		  (?=.*[@#$%])		#   must contains one special symbols in the list "@#$%"
		              .		#     match anything with previous condition checking
		                {6,20}	#        length at least 6 characters and maximum of 20	
		)			# End of group
	*/
	
	/**
	 * Default pattern:
	 * - must contains one digit from 0-9
	 * - must contains one lowercase or uppercase character
	 * - length at least 6 characters and maximum of 20
	 */
	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-zA-Z]).{6,20})";
	private static final Pattern pattern =  Pattern.compile(PASSWORD_PATTERN);
	public static final boolean checkPassword(String password) {
		return checkPassword( pattern, password );
	}
	
	public static final boolean checkPassword(Pattern pattern, String password) {
		return pattern.matcher(password).matches();
	}
	
}
