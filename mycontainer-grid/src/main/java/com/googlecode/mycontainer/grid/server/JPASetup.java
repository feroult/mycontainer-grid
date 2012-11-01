package com.googlecode.mycontainer.grid.server;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.mycontainer.jpa.JPADeployer;
import com.googlecode.mycontainer.jpa.JPAInfoBuilder;

public abstract class JPASetup {

	private String name;

	private String dataSource;

	@SuppressWarnings("rawtypes")
	private List<Class> ejbs = new ArrayList<Class>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	@SuppressWarnings("rawtypes")
	public void addEntityEJBToScan(Class clazz) {
		ejbs.add(clazz);
	}

	public abstract void set(JPADeployer deployer);

	protected void setupNames(JPAInfoBuilder info) {
		info.setPersistenceUnitName(getName());
		info.setJtaDataSourceName(getDataSource());
	}

	@SuppressWarnings("rawtypes")
	protected void setupEJBs(JPAInfoBuilder info) {
		for (Class clazz : ejbs) {
			info.addJarFileUrl(clazz);
			info.setPersistenceUnitRootUrl(clazz);
		}
	}
}
