package com.googlecode.mycontainer.grid.util;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.googlecode.mycontainer.grid.server.MyContainerGrid;

public abstract class GridServiceLocator {

	private static ThreadLocal<String> partitionHolder = new ThreadLocal<String>();

	@SuppressWarnings("unchecked")
	public static <T> T lookup(String jndi, Class<T> clazz) {
		try {
			return (T) getInitialContext().lookup(jndi);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public static InitialContext getInitialContext() throws NamingException {
		return new InitialContext(getServerProperties());
	}

	public static void setPartition(String partition) {
		partitionHolder.set(partition);
	}

	public static String getPartition() {
		return partitionHolder.get();
	}

	public static Properties getServerProperties() {
		String partition = getPartition();
		if (partition != null) {
			return MyContainerGrid.getPartitionProperties(partition);
		}
		return null;
	}
}