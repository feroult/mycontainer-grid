package com.googlecode.mycontainer.grid.testapp;

import com.googlecode.mycontainer.grid.server.HSQLDataSourceSetup;
import com.googlecode.mycontainer.grid.server.HSQLJPASetup;
import com.googlecode.mycontainer.grid.server.JPASetup;
import com.googlecode.mycontainer.grid.server.MyContainerGrid;
import com.googlecode.mycontainer.grid.testapp.ejb.SimpleEntity;
import com.googlecode.mycontainer.grid.testapp.ejb.SimpleService;

public class BootTests {

	public static void main(String[] args) {
		new BootTests().run();
	}

	public void run() {
		MyContainerGrid grid = new MyContainerGrid();

		grid.addStatelesssEJBToScan(SimpleService.class);

		HSQLDataSourceSetup hsql = new HSQLDataSourceSetup();
		hsql.setName("TestDS");
		grid.addDataSourceSetup(hsql);

		JPASetup jpa = new HSQLJPASetup();
		jpa.setName("test-pu");
		jpa.setDataSource("TestDS");
		jpa.addEntityEJBToScan(SimpleEntity.class);
		grid.addJPASetup(jpa);

		grid.addWebContext("/testapp", "src/main/webapp/");

		grid.run();
	}

}
