package com.googlecode.mycontainergrid.server;

import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.googlecode.mycontainer.datasource.DataSourceDeployer;
import com.googlecode.mycontainer.ejb.SessionInterceptorDeployer;
import com.googlecode.mycontainer.ejb.StatelessScannableDeployer;
import com.googlecode.mycontainer.jpa.HibernateJPADeployer;
import com.googlecode.mycontainer.jpa.JPADeployer;
import com.googlecode.mycontainer.jpa.JPAInfoBuilder;
import com.googlecode.mycontainer.kernel.boot.ContainerBuilder;
import com.googlecode.mycontainer.kernel.deploy.ScannerDeployer;
import com.googlecode.mycontainer.web.ContextWebServer;
import com.googlecode.mycontainer.web.Realm;
import com.googlecode.mycontainer.web.jetty.JettyServerDeployer;

public class Server {

	Logger logger = Logger.getLogger(Server.class.getName());

	public void run() {
		try {
			xpto();
			logger.info("mycontainer-grid started");

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void xpto() throws NamingException {
		System.setProperty("java.naming.factory.initial",
				"com.googlecode.mycontainer.kernel.naming.MyContainerContextFactory");

		ContainerBuilder builder = new ContainerBuilder();

		builder.deployVMShutdownHook();

		SessionInterceptorDeployer sessionInterceptorDeployer = builder
				.createDeployer(SessionInterceptorDeployer.class);
		sessionInterceptorDeployer.deploy();

		builder.deployJTA();

		DataSourceDeployer ds = builder
				.createDeployer(DataSourceDeployer.class);
		ds.setName("TestDS");
		ds.setDriver("org.hsqldb.jdbcDriver");
		ds.setUrl("jdbc:hsqldb:mem:.");
		ds.setUser("sa");
		ds.deploy();

		JPADeployer jpa = builder.createDeployer(HibernateJPADeployer.class);
		JPAInfoBuilder info = (JPAInfoBuilder) jpa.getInfo();
		info.setPersistenceUnitName("test-pu");
		info.setJtaDataSourceName("TestDS");
		// info.addJarFileUrl(com.googlecode.mycontainer.test.ejb.CustomerBean.class);
		// info.setPersistenceUnitRootUrl(com.googlecode.mycontainer.test.ejb.CustomerBean.class);
		Properties props = info.getProperties();
		props.setProperty("hibernate.dialect",
				"org.hibernate.dialect.HSQLDialect");
		props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		props.setProperty("hibernate.show_sql", "true");
		jpa.deploy();

		ScannerDeployer scanner = builder.createDeployer(ScannerDeployer.class);
		scanner.add(new StatelessScannableDeployer());
		// scanner.scan(EntityManagerWrapperBean.class);
		scanner.deploy();

		JettyServerDeployer webServer = builder
				.createDeployer(JettyServerDeployer.class);
		webServer.bindPort(8080);
		webServer.setName("WebServer");

		Realm realm = new Realm("testRealm");
		realm.config("teste", "pass", new String[] { "admin", "user" });
		webServer.addRealm(realm);

		ContextWebServer webContext = webServer.createContextWebServer();
		webContext.setContext("/test");
		webContext.setResources("mycontainer-test-web/src/main/webapp/");

		webServer.deploy();

		builder.waitFor();
	}
}
