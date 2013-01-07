package com.googlecode.mycontainer.grid.server.setup;

import java.util.Properties;

import com.googlecode.mycontainer.jpa.JPAInfoBuilder;

public class HSQLJPASetup extends JPASetup {

	public void setupCustomProperties(JPAInfoBuilder info) {
		Properties props = info.getProperties();
		props.setProperty("hibernate.dialect",
				"org.hibernate.dialect.HSQLDialect");
		props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		props.setProperty("hibernate.show_sql", "true");
		props.setProperty("hibernate.transaction.auto_close_session", "true");
		props.setProperty("hibernate.cache.use_second_level_cache", "false");
		props.setProperty("hibernate.cache.use_query_cache", "false");
		props.setProperty("hibernate.jdbc.batch_size", "20");
		props.setProperty("hibernate.default_batch_fetch_size", "8");

	}
}
