package com.googlecode.mycontainer.grid.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.googlecode.mycontainer.grid.infra.BootTests;
import com.googlecode.mycontainer.grid.server.MyContainerGrid;
import com.googlecode.mycontainer.grid.util.GridSeleniumBase;

public class GridSeleniumBaseTest extends GridSeleniumBase {

	@Test
	public void setupTest() {
		Assert.assertEquals(getThreadCount(), 5);
	}

	@Override
	public MyContainerGrid setupMyContainerGrid() {
		BootTests boot = new BootTests();
		return boot.setupMyContainerGrid();
	}	
}
