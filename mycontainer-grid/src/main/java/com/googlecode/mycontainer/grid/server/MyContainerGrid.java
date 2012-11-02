package com.googlecode.mycontainer.grid.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.googlecode.mycontainer.datasource.DataSourceDeployer;
import com.googlecode.mycontainer.ejb.SessionInterceptorDeployer;
import com.googlecode.mycontainer.ejb.StatelessScannableDeployer;
import com.googlecode.mycontainer.jpa.HibernateJPADeployer;
import com.googlecode.mycontainer.jpa.JPADeployer;
import com.googlecode.mycontainer.kernel.boot.ContainerBuilder;
import com.googlecode.mycontainer.kernel.deploy.ScannerDeployer;
import com.googlecode.mycontainer.kernel.naming.MyContainerContextFactory;
import com.googlecode.mycontainer.web.ContextWebServer;
import com.googlecode.mycontainer.web.FilterDesc;
import com.googlecode.mycontainer.web.jetty.JettyServerDeployer;

public class MyContainerGrid {

	Logger logger = Logger.getLogger(MyContainerGrid.class.getName());

	private Map<String, String> webContexts = new HashMap<String, String>();

	private List<DataSourceSetup> dataSourceSetups = new ArrayList<DataSourceSetup>();

	private List<JPASetup> jpaSetups = new ArrayList<JPASetup>();

	@SuppressWarnings("rawtypes")
	private List<Class> ejbs = new ArrayList<Class>();

	public void run(int servers) {
		try {
			deploy(servers);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deploy(int servers) throws NamingException {
		deployMyContainers(servers);
		deployJetty();
	}

	private void deployJetty() throws NamingException {
		ContainerBuilder builder = createContainerBuilder("webserver-partition");
		deployWebServer(builder);
		builder.waitFor();
	}

	private void deployMyContainers(int servers) throws NamingException {
		for (int serverNumber = 1; serverNumber <= servers; serverNumber++) {
			String partition = getPartition(serverNumber);
			ContainerBuilder builder = createContainerBuilder(partition);

			// FIXME faz sentido um hook desses para todos?
			deployVMShutdownHook(builder);
			deployJTA(builder);
			deployDataSources(builder, partition);
			deployJPAs(builder, partition);
			deployStatelessEJBs(builder);

			logger.info("mycontainer-grid started - partition: " + partition);
		}
	}

	private ContainerBuilder createContainerBuilder(String partition)
			throws NamingException {
		Properties serverProperties = getPartitionProperties(partition);

		ContainerBuilder builder = new ContainerBuilder(serverProperties);
		return builder;
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

	private void deployDataSources(ContainerBuilder builder, String partition) {
		for (DataSourceSetup setup : dataSourceSetups) {
			DataSourceDeployer deployer = builder
					.createDeployer(DataSourceDeployer.class);
			setup.set(deployer, partition);
			deployer.deploy();
		}
	}

	private void deployJPAs(ContainerBuilder builder, String partition) {
		for (JPASetup setup : jpaSetups) {
			JPADeployer deployer = builder
					.createDeployer(HibernateJPADeployer.class);
			setup.set(deployer, partition);
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

		FilterDesc partitionSelectorFilter = new FilterDesc(PartitionSelectorFilter.class, "/*");

		for (String context : webContexts.keySet()) {
			ContextWebServer webContext = webServer.createContextWebServer();
			webContext.setContext(context);
			webContext.setResources(webContexts.get(context));
			webContext.getFilters().add(partitionSelectorFilter);
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

	private static String getPartition(int serverNumber) {
		return "partition" + serverNumber;
	}

	public static Properties getPartitionProperties(String partition) {
		Properties serverProperties = new Properties();

		serverProperties
				.setProperty("java.naming.factory.initial",
						"com.googlecode.mycontainer.kernel.naming.MyContainerContextFactory");

		serverProperties.setProperty(
				MyContainerContextFactory.CONTAINER_PARTITION, partition);
		return serverProperties;
	}

}
