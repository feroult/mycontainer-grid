package com.googlecode.mycontainer.grid.server;

import java.util.Properties;

import com.googlecode.mycontainer.jpa.JPAInfoBuilder;

public class HSQLJPASetup extends JPASetup {

	protected void setupCustomProperties(JPAInfoBuilder info) {
		Properties props = info.getProperties();
		props.setProperty("hibernate.dialect",
				"org.hibernate.dialect.HSQLDialect");
		props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		props.setProperty("hibernate.show_sql", "true");
	}
}
