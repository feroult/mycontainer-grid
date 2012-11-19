package com.googlecode.mycontainer.grid.infra;

import com.googlecode.mycontainer.grid.server.MyContainerGrid;
import com.googlecode.mycontainer.grid.server.setup.HSQLDataSourceSetup;
import com.googlecode.mycontainer.grid.server.setup.HSQLJPASetup;
import com.googlecode.mycontainer.grid.server.setup.JPASetup;
import com.googlecode.mycontainer.grid.testapp.ejb.SimpleEntity;
import com.googlecode.mycontainer.grid.testapp.ejb.SimpleService;

public class BootTests {

	public static void main(String[] args) {
		BootTests boot = new BootTests();
		MyContainerGrid grid = boot.setupMyContainerGrid(5);
		grid.run();
	}

	public MyContainerGrid setupMyContainerGrid(int servers) {
		MyContainerGrid grid = new MyContainerGrid(servers);
		setupStatelessServices(grid);
		setupDataSources(grid);
		setupJPAs(grid);
		setupWebContexts(grid);
		return grid;
	}

	private void setupJPAs(MyContainerGrid grid) {
		JPASetup jpa = new HSQLJPASetup();
		jpa.setName("test-pu");
		jpa.setDataSource("TestDS");
		jpa.addEntityEJBToScan(SimpleEntity.class);
		grid.addJPASetup(jpa);
	}

	private void setupDataSources(MyContainerGrid grid) {
		HSQLDataSourceSetup hsql = new HSQLDataSourceSetup();
		hsql.setName("TestDS");
		grid.addDataSourceSetup(hsql);
	}

	private void setupStatelessServices(MyContainerGrid grid) {
		grid.addStatelesssEJBToScan(SimpleService.class);
	}

	private void setupWebContexts(MyContainerGrid grid) {
		grid.addWebContext("/testapp", "src/main/webapp/");
	}
}
