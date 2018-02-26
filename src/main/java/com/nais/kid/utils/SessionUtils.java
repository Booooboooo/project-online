package com.nais.kid.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;


public class SessionUtils {
	public static String base64Encoding(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			//s = new BASE64Encoder().encode(b);
			Base64.getEncoder().encode(b);
		}
		
		return s;
	}
	
	public static String Base64Decode(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			
			try {
				b = Base64.getDecoder().decode(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
