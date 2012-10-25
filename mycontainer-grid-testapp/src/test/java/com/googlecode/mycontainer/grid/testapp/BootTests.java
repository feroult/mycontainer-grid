package com.googlecode.mycontainer.grid.testapp;

import com.googlecode.mycontainer.grid.server.MyContainerGrid;


public class BootTests {

	public void run() {

		MyContainerGrid grid = new MyContainerGrid();

		grid.addWebContext("/testapp", "src/main/webapp/");

		grid.run();

	}

}
