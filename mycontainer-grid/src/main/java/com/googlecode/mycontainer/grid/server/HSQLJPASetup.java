package com.googlecode.mycontainer.grid.server;

import java.util.Properties;

import com.googlecode.mycontainer.jpa.JPADeployer;
import com.googlecode.mycontainer.jpa.JPAInfoBuilder;

public class HSQLJPASetup extends JPASetup {

	public void set(JPADeployer deployer) {
		JPAInfoBuilder info = (JPAInfoBuilder) deployer.getInfo();

		setupNames(info);

		setupEJBs(info);

		setupProperties(info);
	}

	private void setupProperties(JPAInfoBuilder info) {
		Properties props = info.getProperties();
		props.setProperty("hibernate.dialect",
				"org.hibernate.dialect.HSQLDialect");
		props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		props.setProperty("hibernate.show_sql", "true");
	}
}
