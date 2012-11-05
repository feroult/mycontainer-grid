package com.googlecode.mycontainer.grid.testapp;

import com.googlecode.mycontainer.grid.server.MyContainerGrid;
import com.googlecode.mycontainer.grid.server.setup.HSQLDataSourceSetup;
import com.googlecode.mycontainer.grid.server.setup.HSQLJPASetup;
import com.googlecode.mycontainer.grid.server.setup.JPASetup;
import com.googlecode.mycontainer.grid.testapp.ejb.SimpleEntity;
import com.googlecode.mycontainer.grid.testapp.ejb.SimpleService;

public class BootTests {

	public static void main(String[] args) {
		new BootTests().run();
	}

	public void run() {
		MyContainerGrid grid = new MyContainerGrid();

		setupStatelessServices(grid);
		setupDataSources(grid);
		setupJPAs(grid);
		setupWebContexts(grid);

		grid.run(5);
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
