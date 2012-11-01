package com.googlecode.mycontainer.grid.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.googlecode.mycontainer.datasource.DataSourceDeployer;
import com.googlecode.mycontainer.ejb.SessionInterceptorDeployer;
import com.googlecode.mycontainer.ejb.StatelessScannableDeployer;
import com.googlecode.mycontainer.jpa.HibernateJPADeployer;
import com.googlecode.mycontainer.jpa.JPADeployer;
import com.googlecode.mycontainer.kernel.boot.ContainerBuilder;
import com.googlecode.mycontainer.kernel.deploy.ScannerDeployer;
import com.googlecode.mycontainer.web.ContextWebServer;
import com.googlecode.mycontainer.web.jetty.JettyServerDeployer;

public class MyContainerGrid {

	Logger logger = Logger.getLogger(MyContainerGrid.class.getName());

	private Map<String, String> webContexts = new HashMap<String, String>();

	private List<DataSourceSetup> dataSourceSetups = new ArrayList<DataSourceSetup>();

	private List<JPASetup> jpaSetups = new ArrayList<JPASetup>();

	@SuppressWarnings("rawtypes")
	private List<Class> ejbs = new ArrayList<Class>();

	public void run() {
		try {
			deploy();
			logger.info("mycontainer-grid started");

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deploy() throws NamingException {
		System.setProperty("java.naming.factory.initial",
				"com.googlecode.mycontainer.kernel.naming.MyContainerContextFactory");

		ContainerBuilder builder = new ContainerBuilder();

		deployVMShutdownHook(builder);

		deployJTA(builder);

		deployDataSources(builder);

		deployJPAs(builder);

		deployStatelessEJBs(builder);

		deployWebServer(builder);

		builder.waitFor();
	}

	private void deployVMShutdownHook(ContainerBuilder builder) {
		builder.deployVMShutdownHook();
	}

	private void deployJTA(ContainerBuilder builder) {
		SessionInterceptorDeployer sessionInterceptorDeployer = builder
				.createDeployer(SessionInterceptorDeployer.class);
		sessionInterceptorDeployer.deploy();

		builder.deployJTA();
	}

	private void deployDataSources(ContainerBuilder builder) {
		for (DataSourceSetup setup : dataSourceSetups) {
			DataSourceDeployer deployer = builder
					.createDeployer(DataSourceDeployer.class);
			setup.set(deployer);
			deployer.deploy();
		}
	}

	private void deployJPAs(ContainerBuilder builder) {
		for (JPASetup setup : jpaSetups) {
			JPADeployer deployer = builder
					.createDeployer(HibernateJPADeployer.class);
			setup.set(deployer);
			deployer.deploy();
		}
	}

	@SuppressWarnings("rawtypes")
	private void deployStatelessEJBs(ContainerBuilder builder) {
		ScannerDeployer scanner = builder.createDeployer(ScannerDeployer.class);
		scanner.add(new StatelessScannableDeployer());

		for (Class clazz : ejbs) {
			scanner.scan(clazz);
		}

		scanner.deploy();
	}

	private void deployWebServer(ContainerBuilder builder) {
		JettyServerDeployer webServer = builder
				.createDeployer(JettyServerDeployer.class);
		webServer.bindPort(8080);
		webServer.setName("WebServer");

		for (String context : webContexts.keySet()) {
			ContextWebServer webContext = webServer.createContextWebServer();
			webContext.setContext(context);
			webContext.setResources(webContexts.get(context));
		}

		webServer.deploy();
	}

	public void addWebContext(String context, String resources) {
		webContexts.put(context, resources);
	}

	@SuppressWarnings("rawtypes")
	public void addStatelesssEJBToScan(Class clazz) {
		ejbs.add(clazz);
	}

	public void addDataSourceSetup(DataSourceSetup setup) {
		dataSourceSetups.add(setup);
	}

	public void addJPASetup(JPASetup setup) {
		jpaSetups.add(setup);
	}
}
