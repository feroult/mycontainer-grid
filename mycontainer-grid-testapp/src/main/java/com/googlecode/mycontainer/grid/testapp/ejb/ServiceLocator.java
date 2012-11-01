package com.googlecode.mycontainer.grid.testapp.ejb;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.googlecode.mycontainer.grid.server.MyContainerGrid;

// TODO mover service locator para o mycontainer-grid
public abstract class ServiceLocator {

	@SuppressWarnings("unchecked")
	public static <T> T lookup(String jndi, Class<T> clazz) {
		try {
			return (T) new InitialContext(getServerProperties()).lookup(jndi);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}

	}

	private static Properties getServerProperties() {
		return MyContainerGrid.getPartitionProperties("server-1");
	}

}