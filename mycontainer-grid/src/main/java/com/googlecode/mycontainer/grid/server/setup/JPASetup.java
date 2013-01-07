package com.googlecode.mycontainer.grid.server.setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.googlecode.mycontainer.jpa.JPADeployer;
import com.googlecode.mycontainer.jpa.JPAInfoBuilder;

public abstract class JPASetup {

	private String name;

	private String dataSource;

	private Class persistenceUnitRootUrl;

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

	public Class getPersistenceUnitRootUrl() {
		return persistenceUnitRootUrl;
	}

	public void setPersistenceUnitRootUrl(Class persistenceUnitRootUrl) {
		this.persistenceUnitRootUrl = persistenceUnitRootUrl;
	}

	@SuppressWarnings("rawtypes")
	public void addEntityEJBToScan(Class clazz) {
		ejbs.add(clazz);
	}


	public void set(JPADeployer deployer, String partition) {
		JPAInfoBuilder info = (JPAInfoBuilder) deployer.getInfo();

		setupNames(info);

		setupEJBs(info);

		setupCustomProperties(info);

		setupCommonProperties(info, partition);
	}

	protected abstract void setupCustomProperties(JPAInfoBuilder info);

	private void setupCommonProperties(JPAInfoBuilder info, String partition) {
		Properties props = info.getProperties();
		props.setProperty("hibernate.jndi.java.naming.factory.initial", "com.googlecode.mycontainer.kernel.naming.MyContainerContextFactory");
		props.setProperty("hibernate.jndi.com.mycontainer.kernel.naming.partition", partition);
	}

	private void setupNames(JPAInfoBuilder info) {
		info.setPersistenceUnitName(getName());
		info.setJtaDataSourceName(getDataSource());
	}

	@SuppressWarnings("rawtypes")
	private void setupEJBs(JPAInfoBuilder info) {
		for (Class clazz : ejbs) {
			info.addJarFileUrl(clazz);
			//info.setPersistenceUnitRootUrl(clazz);

		}
		if (this.getPersistenceUnitRootUrl()!=null) {
			info.setPersistenceUnitRootUrl(this.getPersistenceUnitRootUrl());
		}

	}
}
