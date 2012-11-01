package com.googlecode.mycontainer.grid.testapp.ejb;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public abstract class ServiceLocator {

	@SuppressWarnings("unchecked")
	public static <T> T lookup(String jndi, Class<T> clazz) {
		try {
			return (T) new InitialContext().lookup(jndi);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}

	}

}