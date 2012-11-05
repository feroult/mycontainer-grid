package com.googlecode.mycontainer.grid.server.setup;

import com.googlecode.mycontainer.datasource.DataSourceDeployer;

public abstract class DataSourceSetup {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract void set(DataSourceDeployer deployer, String partition);

}
