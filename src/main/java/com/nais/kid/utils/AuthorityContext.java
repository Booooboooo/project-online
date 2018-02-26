package com.nais.kid.utils;

import com.nais.kid.keeper.domain.User;

public class AuthorityContext {
	
	private static ThreadLocal<User> threadLocal = new ThreadLocal<>();
	
	
	public static User getUser() {
		return threadLocal.get();
	}
	
	public static void setUser(User umode) {
		 threadLocal.set(umode);
	}
	
	public static void clearUser() {
		threadLocal.remove();
	}
}
