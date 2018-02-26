package com.nais.kid.keeper.domain;

import java.util.HashMap;
import java.util.Map;

public class AdminGroup {
	private static Map<String, String>  admins ;
	
	static {
		admins = new HashMap<> ();
		admins.put("liutong", "wozuimeili");
		admins.put("zhangjilong", "wozuodefanzuihaochi");
		admins.put("lijuming", "woshiCEO");
		admins.put("admin1", "admin1");
		admins.put("admin2", "toor");
		admins.put("chris", "wozuiniubi");
	}
	
	public AdminGroup() {
		
	}
	
	
	public static String getEntry(String username) {
		return admins.get(username);
	}
	
}
