package com.googlecode.mycontainer.grid.server;

import com.googlecode.mycontainer.datasource.DataSourceDeployer;

public class HSQLDataSourceSetup extends DataSourceSetup {

	@Override
	public void set(DataSourceDeployer deployer, String partition) {
		deployer.setName(getName());
		deployer.setDriver("org.hsqldb.jdbcDriver");
		deployer.setUrl("jdbc:hsqldb:mem:" + partition);
		deployer.setUser("sa");
	}

}
