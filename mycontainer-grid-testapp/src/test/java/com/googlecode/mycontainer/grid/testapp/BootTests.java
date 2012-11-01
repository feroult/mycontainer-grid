package com.googlecode.mycontainer.grid.testapp;

import com.googlecode.mycontainer.grid.server.MyContainerGrid;
import com.googlecode.mycontainer.grid.testapp.ejb.SimpleService;

public class BootTests {

	public static void main(String[] args) {
		new BootTests().run();
	}

	public void run() {
		MyContainerGrid grid = new MyContainerGrid();

		grid.addEjb(SimpleService.class);
		grid.addWebContext("/testapp", "src/main/webapp/");

		grid.run();
	}

}
